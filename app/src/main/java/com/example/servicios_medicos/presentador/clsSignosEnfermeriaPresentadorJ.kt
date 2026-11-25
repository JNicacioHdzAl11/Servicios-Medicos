package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.*
import com.example.servicios_medicos.vista.*
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsSignosEnfermeriaPresentadorJ(private val vista: TomaSignosActivityJ) {
    private val modelo = clsModuloEnfermeriaModeloJ()
    private var solicitud: Call<*>? = null

    fun guardar(token: String, datos: dataSignosEnfermeriaJ) {
        if (token.isBlank()) { vista.mostrarError("Necesitas iniciar sesión"); return }
        if (datos.matricula.isBlank()) { vista.mostrarError("Selecciona un alumno"); return }
        if (datos.peso == null && datos.temperatura == null && datos.presion.isNullOrBlank()) { vista.mostrarError("Captura al menos peso, temperatura o presión"); return }
        if (datos.presion != null && !Regex("^[0-9]{2,3}/[0-9]{2,3}$").matches(datos.presion)) { vista.mostrarError("Presión: formato 120/80"); return }
        if ((datos.peso != null && datos.peso !in 1.0..400.0) || (datos.temperatura != null && datos.temperatura !in 30.0..45.0) || (datos.frecuenciaCardiaca != null && datos.frecuenciaCardiaca !in 30..220) || (datos.frecuenciaRespiratoria != null && datos.frecuenciaRespiratoria !in 5..60) || (datos.saturacion != null && datos.saturacion !in 50..100)) { vista.mostrarError("Revisa los rangos permitidos de los signos"); return }
        solicitud?.cancel()
        vista.mostrarCargando(true)
        val llamada = modelo.registrarSignos(token, datos)
        solicitud = llamada
        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<JsonElement>> {
            override fun onResponse(call: Call<dataRespuestaEnfermeriaJ<JsonElement>>, response: Response<dataRespuestaEnfermeriaJ<JsonElement>>) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true) {
                    vista.mostrarGuardado(resultado.mensaje)
                } else {
                    var mensaje = resultado?.mensaje ?: "Error HTTP " + response.code()
                    try {
                        val cuerpo = response.errorBody()?.string()
                        if (!cuerpo.isNullOrBlank()) mensaje = JSONObject(cuerpo).optString("message", mensaje)
                    } catch (e: Exception) { }
                    vista.mostrarError(mensaje)
                }
            }
            override fun onFailure(call: Call<dataRespuestaEnfermeriaJ<JsonElement>>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun cancelar() { solicitud?.cancel() }
}
