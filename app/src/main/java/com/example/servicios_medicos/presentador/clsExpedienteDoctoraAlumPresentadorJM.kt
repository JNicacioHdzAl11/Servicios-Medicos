package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataExpedienteDoctoraJM
import com.example.servicios_medicos.modelo.clsExpedienteDoctoraAlumModeloJM
import com.example.servicios_medicos.vista.ExpedienteMedicoAlumActivityJM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsExpedienteMedicoAlumPresentadorJM(private val vista: ExpedienteMedicoAlumActivityJM) {
    private val modelo = clsExpedienteDoctoraAlumModeloJM()
    private var token = ""
    private var matricula = ""
    private var consulta: Call<dataExpedienteDoctoraJM>? = null

    fun iniciarPantalla(tokenSesion: String?, matriculaAlumno: String?) { // guarda token y matricula del alumno
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        if (matriculaAlumno != null && matriculaAlumno.isNotEmpty()) {
            matricula = matriculaAlumno
        }
    }

    fun cargarExpediente() {
        if (token.isEmpty()) return
        if (matricula.isEmpty()) {
            vista.mostrarError("No se recibió la matrícula del alumno en espera")
            return
        }
        consulta?.cancel()
        val llamada = modelo.obtenerExpedienteAlumno(token, matricula)
        consulta = llamada
        llamada.enqueue(object : Callback<dataExpedienteDoctoraJM> {
            override fun onResponse(
                call: Call<dataExpedienteDoctoraJM>,
                response: Response<dataExpedienteDoctoraJM>
            ) {
                if (call.isCanceled) return
                val datos = response.body()
                if (response.isSuccessful && datos != null && datos.status == true) {
                    mostrarDatos(datos)
                } else if (response.code() == 401) {
                    vista.mostrarError(mensaje = "Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError(
                        mensaje = datos?.message?.takeIf { it.isNotBlank() }
                            ?: "No pudimos cargar el expediente (${response.code()})."
                    )
                }
            }

            override fun onFailure(call: Call<dataExpedienteDoctoraJM>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        })
    }

    private fun mostrarDatos(datos: dataExpedienteDoctoraJM) {
        val bloque = datos.data ?: return
        val alumno = bloque.alumno
        val exp = bloque.expediente
        val historial = bloque.historialConsultas

        val nombre = alumno?.nombreCompleto.orEmpty()
        val iniciales = modelo.armarIniciales(nombre)
        val carrera = listOfNotNull(
            alumno?.area?.takeIf { it.isNotBlank() },
            alumno?.cuatrimestre?.takeIf { it.isNotBlank() }?.let { "${it}°" },
            alumno?.grupo?.takeIf { it.isNotBlank() }
        ).joinToString(" · ")

        vista.mostrarPaciente(
            iniciales,
            nombre,
            modelo.formatearMatriculaEdad(alumno?.matricula.orEmpty(), exp?.edad.orEmpty()),
            carrera
        )
        vista.mostrarClinicos(
            "Alergia: ${exp?.alergias?.ifBlank { "Ninguna" } ?: "Sin registro"}",
            "Tipo de sangre: ${exp?.tipoSangre?.ifBlank { "No especificado" } ?: "Sin registro"}",
            "Enfermedades crónicas: ${exp?.enfermedades?.ifBlank { "Ninguna" } ?: "Sin registro"}"
        )
        vista.mostrarAntecedentes(
            "Alérgenos: ${exp?.alergias?.ifBlank { "Ninguno" } ?: "Sin registro"}",
            "Antecedentes: ${exp?.antecedentes?.ifBlank { "Ninguno" } ?: "Sin registro"}",
            "Tratamiento habitual: Sin registro"
        )

        val c1 = historial.getOrNull(0)?.let { "${it.fechaConsulta ?: ""} · ${it.motivo ?: ""} (${it.diagnostico ?: "Sin diagnóstico"})" }
            ?: "Sin consultas previas"
        val c2 = historial.getOrNull(1)?.let { "${it.fechaConsulta ?: ""} · ${it.motivo ?: ""} (${it.diagnostico ?: "Sin diagnóstico"})" }
            ?: ""
        vista.mostrarConsultas(c1, c2)
    }

    fun cancelar() {
        consulta?.cancel()
    }


}