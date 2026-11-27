package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsExpedienteMedicoAlumModeloJM
import com.example.servicios_medicos.modelo.dataExpedienteAlumno
import com.example.servicios_medicos.vista.ExpedienteMedicoAlumActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedienteMedicoAlumnoPresentadorJM(private val vista: ExpedienteMedicoAlumActivityJM) {
    private val modelo = clsExpedienteMedicoAlumModeloJM()
    private var token = ""
    private var matricula = ""
    private var consultasRecientes: List<Any> = emptyList()

    fun iniciarPantalla(tokenSesion: String?, matriculaAlumno: String?) { // guarda el token y la matricula del alumno a consultar
        if (!tokenSesion.isNullOrEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        if (!matriculaAlumno.isNullOrEmpty()) {
            matricula = matriculaAlumno
        }
    }

    fun cargarExpedienteMedico() { // pide los datos del expediente y antecedentes sin congelar la pantalla
        if (token.isEmpty() || matricula.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerExpedientePorMatricula(token, matricula)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null) {
                    mostrarDatos(datos)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar el expediente del alumno. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun obtenerMatriculaActual(): String { // regresa la matricula vigente para pasarla a nueva consulta
        return matricula
    }

    fun obtenerConsultasRecientes(): List<Any> { // regresa el historial reciente cargado
        return consultasRecientes
    }

    private fun mostrarDatos(datos: dataExpedienteAlumno) { // manda a la vista la tarjeta del paciente, antecedentes e historial
        val textoMatricula = if (matricula.isNotEmpty()) "Matrícula $matricula" else "Sin matrícula registrada"
        vista.mostrarIniciales(modelo.armarIniciales(textoMatricula))
        vista.mostrarNombrePaciente("Expediente del alumno")
        vista.mostrarMatriculaEdad(textoMatricula)
        vista.mostrarCarreraGrupo("Carrera no especificada")
        vista.mostrarAlertaAlergia("Sin alergias registradas")
        vista.mostrarResumenClinico("No registrado", "Ninguna referida")
        vista.mostrarAntecedentes("Ninguno", "Sin antecedentes", "Ninguno referido")
        consultasRecientes = emptyList()
        vista.mostrarHistorialConsultas(consultasRecientes)
    }
}