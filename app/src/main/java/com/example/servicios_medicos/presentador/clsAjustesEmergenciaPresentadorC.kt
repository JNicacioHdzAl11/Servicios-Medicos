package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsConfiguracionModeloC
import com.example.servicios_medicos.modelo.dataPerfilRespuestaC
import com.example.servicios_medicos.modelo.dataOperacionRespuestaC
import com.example.servicios_medicos.vista.AjustesEmergenciaFragmentC
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsAjustesEmergenciaPresentadorC(
    private val vista: AjustesEmergenciaFragmentC
) {

    private val modelo = clsConfiguracionModeloC()

    private var consulta: Call<dataPerfilRespuestaC>? = null
    private var guardado: Call<dataOperacionRespuestaC>? = null

    fun consultarContacto(token: String) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        vista.mostrarCargando(true)

        val llamada = modelo.obtenerConfiguracion(token)

        consulta = llamada

        llamada.enqueue(
            object : Callback<dataPerfilRespuestaC> {

                override fun onResponse(
                    call: Call<dataPerfilRespuestaC>,
                    response: Response<dataPerfilRespuestaC>
                ) {

                    if (call.isCanceled) {
                        return
                    }

                    vista.mostrarCargando(false)

                    val resultado = response.body()

                    if (response.isSuccessful &&
                        resultado?.estado == true &&
                        resultado.datos != null) {

                        vista.mostrarContacto(
                            resultado.datos.nombreContacto,
                            resultado.datos.parentesco,
                            resultado.datos.telefonoContacto
                        )

                    } else {

                        vista.mostrarError(
                            resultado?.mensaje
                                ?: "No se pudo consultar el contacto: "
                                + response.code()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<dataPerfilRespuestaC>,
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

    fun guardarContacto(
        token: String,
        nombre: String,
        parentesco: String,
        telefono: String
    ) {

        if (token.isBlank()) {
            vista.mostrarError("Necesitas iniciar sesión")
            return
        }

        if (nombre.isBlank() ||
            parentesco.isBlank() ||
            telefono.isBlank()) {

            vista.mostrarError("Completa todos los campos")
            return
        }

        val telefonoLimpio = telefono.trim()

        if (!Regex("^\\+?[0-9]{10,15}$").matches(telefonoLimpio)) {

            vista.mostrarError(
                "El teléfono debe contener entre 10 y 15 dígitos"
            )

            return
        }

        vista.mostrarCargando(true)

        val llamada = modelo.guardarContacto(
            token,
            nombre.trim(),
            parentesco.trim(),
            telefonoLimpio
        )

        guardado = llamada

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

                    if (response.isSuccessful &&
                        resultado?.estado == true) {

                        vista.mostrarGuardado()

                    } else {

                        vista.mostrarError(
                            resultado?.mensaje
                                ?: "Error al guardar: " + response.code()
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
        consulta?.cancel()
        guardado?.cancel()
    }
}