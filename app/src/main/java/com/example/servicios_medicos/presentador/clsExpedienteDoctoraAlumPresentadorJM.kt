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
        val exp = datos.data ?: return

        val nombre = exp.nombrePaciente.orEmpty()
        val iniciales = modelo.armarIniciales(nombre)
        val carrera = listOfNotNull(
            exp.carreraGrupo?.takeIf { it.isNotBlank() }
        ).joinToString(" · ")

        vista.mostrarIniciales(iniciales)
        vista.mostrarNombrePaciente(nombre)
        vista.mostrarMatriculaEdad(
            modelo.formatearMatriculaEdad(exp.matricula.orEmpty(), exp.edad.orEmpty())
        )
        vista.mostrarCarreraGrupo(carrera)
        vista.mostrarAlertaAlergia(
            "Alergias: ${exp.alergia.orEmpty().ifBlank { "Ninguna" }}"
        )
        vista.mostrarResumenClinico(
            "Tipo de sangre: ${exp.tipoSangre.orEmpty().ifBlank { "No registrado" }}",
            "Enfermedades crónicas: ${exp.enfermedadesCronicas.orEmpty().ifBlank { "Ninguna" }}"
        )
        vista.mostrarAntecedentes(
            "Alérgenos: ${exp.alergenosDetalle.orEmpty().ifBlank { "Sin registro" }}",
            "Cirugías previas: ${exp.cirugiasPrevias.orEmpty().ifBlank { "Sin registro" }}",
            "Tratamiento habitual: ${exp.tratamientoHabitual.orEmpty().ifBlank { "Sin registro" }}"
        )
        vista.mostrarHistorialConsultas(
            listOfNotNull(
                exp.consulta1?.takeIf { it.isNotBlank() },
                exp.consulta2?.takeIf { it.isNotBlank() }
            )
        )
    }

    fun cancelar() {
        consulta?.cancel()
    }


}