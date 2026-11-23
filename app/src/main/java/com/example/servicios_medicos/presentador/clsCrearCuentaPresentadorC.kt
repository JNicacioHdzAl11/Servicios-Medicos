package com.example.servicios_medicos.presentador
import com.example.servicios_medicos.modelo.clsCrearCuentaModeloC
import com.example.servicios_medicos.vista.CrearCuentaActivityC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
class clsCrearCuentaPresentadorC(private val vista: CrearCuentaActivityC) {

    private val modelo = clsCrearCuentaModeloC()

    fun crearCuenta(
        nombre: String,
        matricula: String,
        correo: String,
        contrasena: String,
        confirmar: String
    ) {

        if (nombre.isBlank() ||
            matricula.isBlank() ||
            correo.isBlank() ||
            contrasena.isBlank() ||
            confirmar.isBlank()) {

            vista.mostrarError("Completa todos los campos")
            return
        }
        if (nombre.trim().split(Regex("\\s+")).size < 2) {
            vista.mostrarError(
                "Escribe tu nombre y apellido paterno"
            )
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {

            vista.mostrarError("Correo no válido")
            return
        }

        if (contrasena.length < 8) {
            vista.mostrarError(
                "La contraseña debe tener mínimo 8 caracteres"
            )
            return
        }

        if (contrasena != confirmar) {
            vista.mostrarError(
                "Las contraseñas no coinciden"
            )
            return
        }

        vista.mostrarCargando(true)

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val respuesta = modelo.crearCuenta(
                    nombre.trim(),
                    matricula.trim(),
                    correo.trim(),
                    contrasena,
                    confirmar
                )

                val resultado = respuesta.body()

                if (respuesta.isSuccessful &&
                    resultado?.estado == true) {

                    vista.mostrarCuentaCreada()

                } else {

                    var mensaje = "No se pudo crear la cuenta"

                    if (resultado != null) {
                        mensaje = resultado.mensaje
                    } else {
                        val error = respuesta.errorBody()?.string()

                        if (error != null) {
                            try {
                                mensaje = JSONObject(error)
                                    .optString("message", mensaje)
                            } catch (e: Exception) {
                                mensaje = "Error del servidor"
                            }
                        }
                    }

                    vista.mostrarError(mensaje)
                }

            } catch (e: Exception) {

                vista.mostrarError(
                    "Error de conexión con el servidor"
                )

            } finally {

                vista.mostrarCargando(false)
            }

        }
    }
}
