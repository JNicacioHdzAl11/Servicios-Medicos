
package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsLoginModeloC
import com.example.servicios_medicos.vista.LoginActivityC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsLoginPresentadorC(private val vista: LoginActivityC) {

    private val modelo = clsLoginModeloC()

    fun iniciarSesion(correo: String, contrasena: String) {

        if (correo.isBlank() || contrasena.isBlank()) {
            vista.mostrarError("Completa todos los campos")
            return
        }

        vista.mostrarCargando(true)

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val respuesta = modelo.iniciarSesion(
                    correo.trim(),
                    contrasena
                )

                val resultado = respuesta.body()

                if (respuesta.isSuccessful && resultado?.estado == true && resultado.datos != null) {

                    vista.mostrarInicioExitoso(resultado.datos)

                } else if (respuesta.code() == 401) {

                    vista.mostrarError(
                        "Correo o contraseña incorrectos"
                    )

                } else {

                    vista.mostrarError(
                        resultado?.mensaje
                            ?: "No se pudo iniciar sesión"
                    )
                }

            } catch (e: Exception) {

                vista.mostrarError(
                    "Error de conexión: " + e.localizedMessage
                )

            } finally {

                vista.mostrarCargando(false)

            }
        }
    }
}
