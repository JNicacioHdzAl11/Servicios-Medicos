package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsCrearCuentaModeloC
import com.example.servicios_medicos.vista.CrearCuentaActivityC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class clsCrearCuentaPresentadorC(private val vista: CrearCuentaActivityC) {

    private val modelo = clsCrearCuentaModeloC()

    fun cargarCatalogos() {

        vista.mostrarCargandoCatalogos(true)

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val respuesta = modelo.obtenerCatalogos()
                val resultado = respuesta.body()

                if (respuesta.isSuccessful &&
                    resultado?.estado == true &&
                    resultado.datos != null) {

                    vista.mostrarCatalogos(resultado.datos)

                } else {

                    vista.mostrarError(
                        resultado?.mensaje ?: "No se pudieron cargar los campos"
                    )
                }

            } catch (e: Exception) {

                vista.mostrarError("Error de conexión con el servidor")

            } finally {

                vista.mostrarCargandoCatalogos(false)
            }
        }
    }

    fun crearCuenta(
        nombre: String,
        matricula: String,
        correo: String,
        contrasena: String,
        confirmar: String,
        idArea: Int,
        idGrupo: Int,
        idCuatrimestre: Int
    ) {

        if (nombre.isBlank() ||
            matricula.isBlank() ||
            correo.isBlank() ||
            contrasena.isBlank() ||
            confirmar.isBlank()) {

            vista.mostrarError("Completa todos los campos")
            return
        }

        if (idArea <= 0 || idGrupo <= 0 || idCuatrimestre <= 0) {

            vista.mostrarError("Selecciona los campos académicos")
            return
        }

        if (nombre.trim().split(Regex("\\s+")).size < 2) {

            vista.mostrarError("Escribe tu nombre y apellido paterno")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {

            vista.mostrarError("Correo no válido")
            return
        }

        if (contrasena.length < 8) {

            vista.mostrarError("La contraseña debe tener mínimo 8 caracteres")
            return
        }

        if (contrasena != confirmar) {

            vista.mostrarError("Las contraseñas no coinciden")
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
                    confirmar,
                    idArea,
                    idGrupo,
                    idCuatrimestre
                )

                val resultado = respuesta.body()

                if (respuesta.isSuccessful && resultado?.estado == true) {

                    vista.mostrarCuentaCreada()

                } else {

                    var mensaje = resultado?.mensaje ?: "No se pudo crear la cuenta"

                    val error = respuesta.errorBody()?.string()

                    if (error != null) {

                        try {

                            mensaje = JSONObject(error)
                                .optString("message", mensaje)

                        } catch (e: Exception) {

                            mensaje = "Error del servidor"
                        }
                    }

                    vista.mostrarError(mensaje)
                }

            } catch (e: Exception) {

                vista.mostrarError("Error de conexión con el servidor")

            } finally {

                vista.mostrarCargando(false)
            }
        }
    }
}