package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsModuloEnfermeriaModeloJ
import com.example.servicios_medicos.Data.dataSignosEnfermeriaJ
import com.example.servicios_medicos.Data.dataRespuestaEnfermeriaJ
import com.example.servicios_medicos.vista.TomaSignosActivityJ
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsSignosEnfermeriaPresentadorJ(private val vista: TomaSignosActivityJ) {

    private val modelo = clsModuloEnfermeriaModeloJ()
    private var solicitud: Call<dataRespuestaEnfermeriaJ<JsonElement>>? = null

    fun guardar(token: String, datos: dataSignosEnfermeriaJ) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        if (datos.matricula.isBlank()) {
            vista.mostrarError("Selecciona un alumno")
            return
        }

        if (!validarSignos(datos)) {
            return
        }

        cancelar()

        vista.mostrarCargando(true)

        val llamada = modelo.registrarSignos(token, datos)

        solicitud = llamada

        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<JsonElement>> {

            override fun onResponse(
                call: Call<dataRespuestaEnfermeriaJ<JsonElement>>,
                response: Response<dataRespuestaEnfermeriaJ<JsonElement>>
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                val resultado = response.body()

                if (response.isSuccessful && resultado?.estado == true) {

                    vista.mostrarGuardado(resultado.mensaje)

                } else {

                    vista.mostrarError(obtenerMensajeError(response))
                }
            }

            override fun onFailure(
                call: Call<dataRespuestaEnfermeriaJ<JsonElement>>,
                t: Throwable
            ) {

                if (call.isCanceled) {
                    return
                }

                vista.mostrarCargando(false)

                vista.mostrarError("Error de conexión: " + t.localizedMessage)
            }
        })
    }

    private fun validarSignos(datos: dataSignosEnfermeriaJ): Boolean {

        if (datos.peso == null && datos.temperatura == null &&
            datos.presion.isNullOrBlank()) {

            vista.mostrarError("Captura al menos peso, temperatura o presión")
            return false
        }

        if (datos.presion != null &&
            !Regex("^[0-9]{2,3}/[0-9]{2,3}$").matches(datos.presion)) {

            vista.mostrarError("Presión: formato 120/80")
            return false
        }

        if (datos.peso != null && datos.peso !in 1.0..400.0) {
            vista.mostrarError("Peso incorrecto")
            return false
        }

        if (datos.temperatura != null && datos.temperatura !in 30.0..45.0) {
            vista.mostrarError("Temperatura incorrecta")
            return false
        }

        if (datos.frecuenciaCardiaca != null && datos.frecuenciaCardiaca !in 30..220) {
            vista.mostrarError("Frecuencia cardíaca incorrecta")
            return false
        }

        if (datos.frecuenciaRespiratoria != null && datos.frecuenciaRespiratoria !in 5..60) {
            vista.mostrarError("Frecuencia respiratoria incorrecta")
            return false
        }

        if (datos.saturacion != null && datos.saturacion !in 50..100) {
            vista.mostrarError("Saturación incorrecta")
            return false
        }

        return true
    }

    private fun obtenerMensajeError(
        response: Response<dataRespuestaEnfermeriaJ<JsonElement>>
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