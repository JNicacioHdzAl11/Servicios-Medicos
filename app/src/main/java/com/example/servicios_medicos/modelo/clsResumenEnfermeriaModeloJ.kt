package com.example.servicios_medicos.modelo

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Registro temporal de valoraciones confirmadas por el servidor en esta sesión.
object clsResumenEnfermeriaModeloJ {
    data class Atencion(val nombre: String, val fecha: String, val hora: String)
    private var sesion = ""
    private val registros = ArrayList<Atencion>()
    private fun preparar(token: String) {
        if (sesion != token) { registros.clear(); sesion = token }
    }
    fun fechaHoy(): String = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date())
    fun registrar(token: String, nombre: String) {
        preparar(token)
        registros.add(0, Atencion(nombre, fechaHoy(), SimpleDateFormat("HH:mm", Locale.ROOT).format(Date())))
    }
    fun consultar(token: String): List<Atencion> {
        preparar(token)
        val hoy = ArrayList<Atencion>()
        for (registro in registros) if (registro.fecha == fechaHoy()) hoy.add(registro)
        return hoy
    }
}
