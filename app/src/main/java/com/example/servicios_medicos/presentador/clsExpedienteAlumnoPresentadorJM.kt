package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsExpedienteAlumnoModeloJM
import com.example.servicios_medicos.vista.ExpedienteAlumnoActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedienteAlumnoPresentadorJM(private val vista: ExpedienteAlumnoActivityJM) {
    private val modelo = clsExpedienteAlumnoModeloJM()
    private var token = ""
    private var matricula = ""

    fun iniciarPantalla(tokenSesion: String?, matriculaAlumno: String? = "") { // guarda el token y la matricula de sesion
        if (!tokenSesion.isNullOrEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        if (!matriculaAlumno.isNullOrEmpty()) {
            matricula = matriculaAlumno
        }
    }

    fun cargarExpedienteAlumno() { // pide los datos del expediente del alumno sin congelar la pantalla
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerExpedienteAlumno(token)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null) {
                    vista.mostrarDatosAlumno(
                        nombre = "Expediente cargado",
                        matriculaActualizacion = if (matricula.isNotEmpty()) "Matrícula $matricula" else "Expediente actualizado"
                    )
                    vista.mostrarDatosClinicosClave(
                        tipoSangre = "--",
                        alergias = "Sin registro"
                    )
                    vista.mostrarAntecedentes(
                        padecimientos = "Sin registro",
                        cirugias = "Sin antecedentes",
                        tratamiento = "Sin registro activo"
                    )
                    vista.mostrarConsultasRecientes(emptyList())
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar tu expediente. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun solicitarExpedienteCompleto() { // envia la solicitud para obtener el expediente completo
        if (token.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.solicitarExpedienteCompleto(token, matricula)
                if (respuesta.isSuccessful) {
                    vista.mostrarMensajeSolicitud("Solicitud de expediente completo enviada.")
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No se pudo enviar la solicitud. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun obtenerMatriculaActual(): String { // regresa la matricula cargada del alumno
        return matricula
    }
}