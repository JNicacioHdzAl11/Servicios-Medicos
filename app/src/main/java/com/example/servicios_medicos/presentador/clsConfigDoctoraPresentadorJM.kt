package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataConfigDoctoraRespuestaJM
import com.example.servicios_medicos.Data.dataOperacionDoctoraJM
import com.example.servicios_medicos.modelo.clsConfigDoctoraModeloJM
import com.example.servicios_medicos.vista.ConfiguracionDoctoraActivityJM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsConfigDoctoraPresentadorJM(private val vista: ConfiguracionDoctoraActivityJM) {
    private val modelo = clsConfigDoctoraModeloJM()
    private var consulta: Call<dataConfigDoctoraRespuestaJM>? = null
    private var guardado: Call<dataOperacionDoctoraJM>? = null

    fun cargarConfiguracion(token: String) {
        if (token.isBlank()) {
            vista.mostrarError("Inicia sesión para consultar tu configuración")
            return
        }
        consulta?.cancel()
        vista.mostrarCargando(true)
        val llamada = modelo.obtenerConfiguracion(token)
        consulta = llamada
        llamada.enqueue(object : Callback<dataConfigDoctoraRespuestaJM> {
            override fun onResponse(
                call: Call<dataConfigDoctoraRespuestaJM>,
                response: Response<dataConfigDoctoraRespuestaJM>
            ) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.status == true && resultado.data != null) {
                    vista.mostrarConfiguracion(resultado.data)
                } else {
                    vista.mostrarError(
                        if (resultado != null && resultado.message.isNotBlank()) resultado.message
                        else "No fue posible consultar tu configuración (${response.code()})"
                    )
                }
            }

            override fun onFailure(call: Call<dataConfigDoctoraRespuestaJM>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun guardarPreferencias(token: String, modoOscuro: Boolean, avisosUrgencias: Boolean) {
        if (token.isBlank()) {
            vista.mostrarError("La sesión ha caducado")
            return
        }
        consulta?.cancel()
        guardado?.cancel()
        vista.mostrarGuardando(true)
        val llamada = modelo.guardarPreferencias(token, modoOscuro, avisosUrgencias)
        guardado = llamada
        llamada.enqueue(object : Callback<dataOperacionDoctoraJM> {
            override fun onResponse(
                call: Call<dataOperacionDoctoraJM>,
                response: Response<dataOperacionDoctoraJM>
            ) {
                if (call.isCanceled) return
                vista.mostrarGuardando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.status == true) {
                    vista.mostrarPreferenciasGuardadas(modoOscuro, avisosUrgencias)
                } else {
                    vista.mostrarError(resultado?.message ?: "No se guardaron las preferencias")
                    cargarConfiguracion(token)
                }
            }

            override fun onFailure(call: Call<dataOperacionDoctoraJM>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarGuardando(false)
                vista.mostrarError("Sin conexión; no se guardaron los cambios")
                cargarConfiguracion(token)
            }
        })
    }

    fun cancelar() {
        consulta?.cancel()
        guardado?.cancel()
    }
}