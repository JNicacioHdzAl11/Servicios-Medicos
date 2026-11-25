package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.*
import com.example.servicios_medicos.vista.*
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsAlumnosEnfermeriaPresentadorJ(private val vista: BuscarAlumnoActivityJ) {
    private val modelo = clsModuloEnfermeriaModeloJ()
    private var solicitud: Call<*>? = null

    fun cargar(token: String) {
        if (token.isBlank()) { vista.mostrarError("Necesitas iniciar sesión"); return }
        
        solicitud?.cancel()
        vista.mostrarCargando(true)
        val llamada = modelo.listarAlumnos(token)
        solicitud = llamada
        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>> {
            override fun onResponse(call: Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>, response: Response<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true) {
                    vista.mostrarAlumnos(resultado.datos ?: emptyList())
                } else {
                    var mensaje = resultado?.mensaje ?: "Error HTTP " + response.code()
                    try {
                        val cuerpo = response.errorBody()?.string()
                        if (!cuerpo.isNullOrBlank()) mensaje = JSONObject(cuerpo).optString("message", mensaje)
                    } catch (e: Exception) { }
                    vista.mostrarError(mensaje)
                }
            }
            override fun onFailure(call: Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun consultarCitas(token: String, alumno: dataAlumnoEnfermeriaJ) {
        solicitud?.cancel()
        vista.mostrarCargando(true)
        val llamada = modelo.citasPendientes(token)
        solicitud = llamada
        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>> {
            override fun onResponse(call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>, response: Response<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true) {
                    val citas = ArrayList<dataCitaEnfermeriaJ>()
                    for (cita in resultado.datos.orEmpty()) {
                        if (cita.matricula == alumno.matricula) citas.add(cita)
                    }
                    vista.mostrarOpcionesAtencion(alumno, citas)
                } else {
                    vista.mostrarError(resultado?.mensaje ?: "Error al consultar citas: HTTP " + response.code())
                }
            }
            override fun onFailure(call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                vista.mostrarError("No se pudo consultar las citas")
            }
        })
    }

    fun cancelar() { solicitud?.cancel() }
}
