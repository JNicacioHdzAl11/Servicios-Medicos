package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsModuloEnfermeriaModeloJ
import com.example.servicios_medicos.Data.dataRespuestaEnfermeriaJ
import com.example.servicios_medicos.Data.dataAlumnoEnfermeriaJ
import com.example.servicios_medicos.Data.dataCitaEnfermeriaJ
import com.example.servicios_medicos.vista.BuscarAlumnoActivityJ
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsAlumnosEnfermeriaPresentadorJ(private val vista: BuscarAlumnoActivityJ) {

    private val modelo = clsModuloEnfermeriaModeloJ()
    private var solicitud: Call<*>? = null

    fun cargar(token: String) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        cancelar()

        vista.mostrarCargando(true)

        val llamada = modelo.listarAlumnos(token)

        solicitud = llamada

        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>> {

            override fun onResponse(
                call: Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>,
                response: Response<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                val resultado = response.body()

                if (response.isSuccessful && resultado?.estado == true) {

                    val alumnos = resultado.datos ?: emptyList()

                    vista.mostrarAlumnos(alumnos)

                } else {

                    vista.mostrarError(obtenerMensajeError(response))
                }
            }

            override fun onFailure(
                call: Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>,
                t: Throwable
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun consultarCitas(token: String, alumno: dataAlumnoEnfermeriaJ) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        cancelar()

        vista.mostrarCargando(true)

        val llamada = modelo.citasPendientes(token)

        solicitud = llamada

        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>> {

            override fun onResponse(
                call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>,
                response: Response<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                val resultado = response.body()

                if (response.isSuccessful && resultado?.estado == true) {

                    val citas = ArrayList<dataCitaEnfermeriaJ>()

                    val citasRecibidas = resultado.datos ?: emptyList()

                    for (cita in citasRecibidas) {

                        if (cita.matricula == alumno.matricula) {

                            citas.add(cita)
                        }
                    }

                    vista.mostrarOpcionesAtencion(alumno, citas)

                } else {

                    vista.mostrarError(obtenerMensajeError(response))
                }
            }

            override fun onFailure(
                call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>,
                t: Throwable
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                vista.mostrarError("No se pudo consultar las citas")
            }
        })
    }

    private fun obtenerMensajeError(
        response: Response<out dataRespuestaEnfermeriaJ<*>>
    ): String {

        val mensaje = response.body()?.mensaje ?: "Error HTTP " + response.code()

        try {

            val cuerpo = response.errorBody()?.string()

            if (!cuerpo.isNullOrBlank()) {

                return JSONObject(cuerpo).optString("message", mensaje)
            }

        } catch (e: Exception) {
            return mensaje
        }

        return mensaje
    }

    fun cancelar() {

        solicitud?.cancel()
    }
}