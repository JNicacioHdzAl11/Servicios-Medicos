package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsExpedienteModeloJM
import com.example.servicios_medicos.vista.ExpedienteActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedientePresentadorJM(private val vista: ExpedienteActivityJM) {
    private val modelo = clsExpedienteModeloJM()

    fun cargarExpediente(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerExpediente(token)
                val cuerpo = respuesta.body()

                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status && cuerpo.data != null) {
                    vista.mostrarExpediente(cuerpo.data)
                } else {
                    val mensaje = cuerpo?.message ?: "No se pudo cargar el expediente (${respuesta.code()})"
                    vista.mostrarError(mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + e.localizedMessage)
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }

    fun cargarHistorial(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerHistorial(token)
                val cuerpo = respuesta.body()

                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status) {
                    vista.mostrarHistorial(cuerpo.consultas)
                } else {
                    val mensaje = cuerpo?.message ?: "No se pudo cargar el historial (${respuesta.code()})"
                    vista.mostrarError(mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + e.localizedMessage)
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }

    fun descargarReceta(token: String, idConsulta: Int) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerReceta(token, idConsulta)
                val cuerpo = respuesta.body()

                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status && cuerpo.receta != null) {
                    vista.mostrarDetalleReceta(
                        cuerpo.receta.medicamentos,
                        cuerpo.receta.indicaciones,
                        cuerpo.receta.urlPdf
                    )
                } else {
                    val mensaje = cuerpo?.message ?: "No se pudo obtener la receta (${respuesta.code()})"
                    vista.mostrarError(mensaje)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + e.localizedMessage)
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }
}