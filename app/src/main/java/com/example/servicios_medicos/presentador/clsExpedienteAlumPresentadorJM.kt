package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsExpedienteAlumnoModeloJM
import com.example.servicios_medicos.vista.expedienteAlumnoJ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedienteAlumPresentadorJM(private val vista: expedienteAlumnoJ) {

    private val modelo = clsExpedienteAlumnoModeloJM()

    fun cargarDatosExpediente(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // 1. Obtener datos generales del expediente
                val respExp = modelo.obtenerExpediente(token)
                val bodyExp = respExp.body()
                val contenedor = bodyExp?.data

                if (respExp.isSuccessful && bodyExp != null && bodyExp.status && contenedor?.expediente != null) {
                    vista.mostrarDatosExpediente(contenedor.expediente, contenedor.ultimaSolicitud)
                } else {
                    val msg = bodyExp?.message ?: "No se pudo cargar el expediente (${respExp.code()})"
                    vista.mostrarError(msg)
                }

                // 2. Obtener consultas recientes (tomamos las 2 más recientes)
                val respHist = modelo.obtenerHistorialConsultas(token)
                val bodyHist = respHist.body()

                if (respHist.isSuccessful && bodyHist != null && bodyHist.status) {
                    val recientes = if (bodyHist.data.size > 2) {
                        bodyHist.data.subList(0, 2)
                    } else {
                        bodyHist.data
                    }
                    vista.mostrarConsultasRecientes(recientes)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + e.localizedMessage)
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }
}