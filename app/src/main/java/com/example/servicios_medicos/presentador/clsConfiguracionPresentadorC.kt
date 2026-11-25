package com.example.servicios_medicos.presentador
import com.example.servicios_medicos.modelo.clsConfiguracionModeloC
import com.example.servicios_medicos.modelo.dataPerfilRespuestaC
import com.example.servicios_medicos.modelo.dataOperacionRespuestaC
import com.example.servicios_medicos.vista.ConfiguracionActivityC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsConfiguracionPresentadorC(private val vista: ConfiguracionActivityC) {
    private val modelo = clsConfiguracionModeloC()
    private var consulta: Call<dataPerfilRespuestaC>? = null
    private var guardado: Call<dataOperacionRespuestaC>? = null

    fun cargarConfiguracion(token: String) {
        if (token.isBlank()) {
            vista.mostrarError("Inicia sesión para consultar tu configuración")
            return
        }
        consulta?.cancel()
        vista.mostrarCargando(true)
        val llamada = modelo.obtenerConfiguracion(token)
        consulta = llamada
        llamada.enqueue(object : Callback<dataPerfilRespuestaC> {
            override fun onResponse(call: Call<dataPerfilRespuestaC>, response: Response<dataPerfilRespuestaC>) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true && resultado.datos != null) {
                    vista.mostrarConfiguracion(resultado.datos)
                } else {
                    vista.mostrarError(if (resultado != null && resultado.mensaje.isNotBlank()) resultado.mensaje else "No fue posible consultar tu configuración (${response.code()})")
                }
            }
            override fun onFailure(call: Call<dataPerfilRespuestaC>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarCargando(false)
                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun guardarNotificaciones(token: String, recordatorios: Boolean, campanias: Boolean) {
        if (token.isBlank()) {
            vista.mostrarError("La sesión ha caducado")
            return
        }
        consulta?.cancel()
        guardado?.cancel()
        vista.mostrarGuardando(true)
        val llamada = modelo.guardarPreferencias(token, recordatorios, campanias)
        guardado = llamada
        llamada.enqueue(object : Callback<dataOperacionRespuestaC> {
            override fun onResponse(call: Call<dataOperacionRespuestaC>, response: Response<dataOperacionRespuestaC>) {
                if (call.isCanceled) return
                vista.mostrarGuardando(false)
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true) {
                    vista.mostrarPreferenciasGuardadas(recordatorios, campanias)
                } else {
                    vista.mostrarError(resultado?.mensaje ?: "No se guardaron las preferencias")
                    cargarConfiguracion(token)
                }
            }
            override fun onFailure(call: Call<dataOperacionRespuestaC>, t: Throwable) {
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
