package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsInicioAlumnoModeloN
import com.example.servicios_medicos.vista.InicioAlumnoFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsInicioAlumnoPresentadorN(private val vista: InicioAlumnoFragmentN) {
    private val modelo = clsInicioAlumnoModeloN()

    fun cargarDatosInicio(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDatosInicio(token)
                val resultado = respuesta.body()

                if (respuesta.isSuccessful && resultado != null && resultado.status && resultado.data != null) {
                    val datos = resultado.data
                    vista.mostrarDatosAlumno(datos.alumno)
                    vista.mostrarProximaCita(datos.proximaCita)
                    vista.mostrarCampanias(datos.campaniasActivas)
                } else {
                    val msg = resultado?.message ?: "No se pudieron cargar los datos (${respuesta.code()})"
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