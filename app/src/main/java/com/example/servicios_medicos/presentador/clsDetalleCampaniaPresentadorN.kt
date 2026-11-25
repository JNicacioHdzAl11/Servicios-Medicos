package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsDetalleCampaniaModeloN
import com.example.servicios_medicos.vista.DetalleCampaniaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsDetalleCampaniaPresentadorN(private val vista: DetalleCampaniaActivityN) {

    private val modelo = clsDetalleCampaniaModeloN()

    fun cargarDetalleCampania(token: String, idCampania: Int) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDetalleCampania(token, idCampania)
                val resultado = respuesta.body()
                val detalle = resultado?.data

                if (respuesta.isSuccessful && resultado != null && resultado.status && detalle != null) {
                    vista.mostrarDetalleCampania(detalle)
                } else {
                    val msg = resultado?.message ?: "No se pudo obtener el detalle (${respuesta.code()})"
                    vista.mostrarError(msg)
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: " + e.localizedMessage)
            } finally {
                vista.mostrarCargando(false)
            }
        }
    }
}