package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataExpedienteDoctoraJM
import com.example.servicios_medicos.modelo.clsExpedienteAlumnoModeloJM
import com.example.servicios_medicos.vista.ExpedienteAlumnoActivityJM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsExpedienteAlumnoPresentadorJM(private val vista: ExpedienteAlumnoActivityJM) {
    private val modelo = clsExpedienteAlumnoModeloJM()
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

    fun cargarExpediente() { // pide los datos del expediente sin congelar la pantalla
        if (token.isEmpty() || matricula.isEmpty()) {
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
                if (response.isSuccessful && datos != null && datos.status) {
                    mostrarDatos(datos)
                } else if (response.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError(
                        if (datos != null && datos.message.isNotBlank()) datos.message
                        else "No pudimos cargar el expediente. Intenta de nuevo."
                    )
                }
            }

            override fun onFailure(call: Call<dataExpedienteDoctoraJM>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        })
    }

    fun cancelar() {
        consulta?.cancel()
    }

    private fun mostrarDatos(datos: dataExpedienteDoctoraJM) { // manda a la vista los datos del alumno y antecedentes
        val exp = datos.data
        if (exp == null) {
            return
        }

        val iniciales = if (exp.iniciales.isNotEmpty()) {
            exp.iniciales
        } else {
            modelo.armarIniciales(exp.nombrePaciente)
        }
        vista.mostrarPaciente(
            iniciales,
            exp.nombrePaciente,
            modelo.formatearMatriculaEdad(exp.matricula, exp.edad),
            exp.carreraGrupo
        )
        vista.mostrarClinicos(
            "Alergia: ${exp.alergia}",
            "Tipo de sangre: ${exp.tipoSangre}",
            "Enfermedades crónicas: ${exp.enfermedadesCronicas}"
        )
        vista.mostrarAntecedentes(
            "Alérgenos: ${exp.alergenosDetalle}",
            "Cirugías previas: ${exp.cirugiasPrevias}",
            "Tratamiento habitual: ${exp.tratamientoHabitual}"
        )
        vista.mostrarConsultas(exp.consulta1, exp.consulta2)
    }
}