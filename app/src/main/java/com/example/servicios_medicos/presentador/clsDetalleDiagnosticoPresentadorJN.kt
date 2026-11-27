package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsDetalleDiagnosticoModeloJN
import com.example.servicios_medicos.vista.DetalleDiagnosticoJN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsDetalleDiagnosticoPresentadorJN(private val vista: DetalleDiagnosticoJN) {

    private val modelo = clsDetalleDiagnosticoModeloJN()

    fun cargarDetalleDiagnostico(token: String, idConsulta: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val resp = modelo.obtenerDetalleDiagnostico(token, idConsulta)
                val body = resp.body()
                val detalle = body?.data

                if (resp.isSuccessful && body != null && body.status && detalle != null) {
                    vista.mostrarDetalleDiagnostico(detalle)
                } else {
                    val msg = body?.message ?: "No se pudo cargar el diagnóstico (${resp.code()})"
                    vista.mostrarError(msg)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: ${e.localizedMessage}")
            }
        }
    }
}