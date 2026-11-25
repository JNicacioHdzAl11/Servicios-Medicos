package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsCampaniasModeloN
import com.example.servicios_medicos.modelo.dataCampaniaJN
import com.example.servicios_medicos.vista.campanias_jn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsCampaniasPresentadorN(private val vista: campanias_jn) {
    private val modelo = clsCampaniasModeloN()

    fun cargarCampanias(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerCampanias(token)
                val resultado = respuesta.body()
                val listaCompleta = resultado?.data

                if (respuesta.isSuccessful && resultado != null && resultado.status && listaCompleta != null) {
                    val destacada: dataCampaniaJN? = if (listaCompleta.isNotEmpty()) listaCompleta[0] else null
                    val secundarias: List<dataCampaniaJN> = if (listaCompleta.size > 1) {
                        listaCompleta.subList(1, listaCompleta.size)
                    } else {
                        emptyList()
                    }

                    vista.mostrarCampanias(destacada, secundarias)
                } else {
                    val msg = resultado?.message ?: "No se pudieron cargar las campañas (${respuesta.code()})"
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