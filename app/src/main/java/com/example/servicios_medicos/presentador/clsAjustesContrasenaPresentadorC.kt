package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsConfiguracionModeloC
import com.example.servicios_medicos.Data.dataOperacionRespuestaC
import com.example.servicios_medicos.vista.AjustesContrasenaFragmentC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsAjustesContrasenaPresentadorC(
    private val vista: AjustesContrasenaFragmentC
) {

    private val modelo = clsConfiguracionModeloC()

    private var solicitud: Call<dataOperacionRespuestaC>? = null

    fun actualizar(
        token: String,
        actual: String,
        nueva: String,
        confirmar: String
    ) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        if (actual.isBlank() || nueva.isBlank() || confirmar.isBlank()) {
            vista.mostrarError("Completa todos los campos")
            return
        }

        if (nueva.length < 8) {
            vista.mostrarError("Utiliza al menos 8 caracteres")
            return
        }

        if (!Regex(".*[A-Za-z].*").matches(nueva) ||
            !Regex(".*[0-9].*").matches(nueva)) {

            vista.mostrarError("Utiliza letras y números")
            return
        }

        if (nueva != confirmar) {
            vista.mostrarError("Las contraseñas no coinciden")
            return
        }

        if (actual == nueva) {
            vista.mostrarError("La nueva contraseña debe ser diferente")
            return
        }

        vista.mostrarCargando(true)

        val llamada = modelo.actualizarContrasena(
            token,
            actual,
            nueva,
            confirmar
        )

        solicitud = llamada

        llamada.enqueue(
            object : Callback<dataOperacionRespuestaC> {

                override fun onResponse(
                    call: Call<dataOperacionRespuestaC>,
                    response: Response<dataOperacionRespuestaC>
                ) {

                    if (call.isCanceled) {
                        return
                    }

                    vista.mostrarCargando(false)

                    val resultado = response.body()

                    if (response.isSuccessful && resultado?.estado == true) {

                        vista.mostrarContrasenaActualizada()

                    } else {

                        vista.mostrarError(
                            resultado?.mensaje
                                ?: "Error del servidor: " + response.code()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<dataOperacionRespuestaC>,
                    t: Throwable
                ) {

                    if (call.isCanceled) {
                        return
                    }

                    vista.mostrarCargando(false)

                    vista.mostrarError(
                        "Error de conexión: " + t.localizedMessage
                    )
                }
            }
        )
    }

    fun cancelar() {
        solicitud?.cancel()
    }
}