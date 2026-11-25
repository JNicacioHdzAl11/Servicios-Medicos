package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsPerfilModeloJM
import com.example.servicios_medicos.vista.perfilAlumnoN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsPerfilPresentadorJM(private val vista: perfilAlumnoN) {
    private val modelo = clsPerfilModeloJM()

    fun cargarPerfil(token: String) {
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerPerfil(token)
                val resultado = respuesta.body()

                if (respuesta.isSuccessful && resultado != null && resultado.status && resultado.data != null) {
                    vista.mostrarDatosPerfil(resultado.data)
                } else {
                    val msg = resultado?.message ?: "No se pudo cargar el perfil (${respuesta.code()})"
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