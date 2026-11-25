package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsExpedienteAlumnoModeloJM
import com.example.servicios_medicos.vista.ExpedienteAlumActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedienteAlumPresentadorJM(private val vista: ExpedienteAlumActivityJM) {
    private val modelo = clsExpedienteAlumnoModeloJM()

    fun realizarBusqueda(token: String, matricula: String) {
        if (matricula.isEmpty()) {
            vista.mostrarError("Ingresa una matrícula válida")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerExpediente(token, matricula)
                val cuerpo = respuesta.body()

                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status && cuerpo.data != null) {
                    vista.mostrarExpedienteAlumno(cuerpo.data)
                } else {
                    vista.mostrarError(cuerpo?.message ?: "No se encontró el expediente (${respuesta.code()})")
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: ${e.localizedMessage}")
            }
        }
    }

    fun consultarRecetaAnterior(token: String, idConsulta: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerRecetaAnterior(token, idConsulta)
                val cuerpo = respuesta.body()

                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status && cuerpo.receta != null) {
                    vista.recetaCargada(cuerpo.receta.medicamentos, cuerpo.receta.indicaciones)
                } else {
                    vista.mostrarError(cuerpo?.message ?: "No se pudo cargar la receta (${respuesta.code()})")
                }
            } catch (e: Exception) {
                vista.mostrarError("Error de conexión: ${e.localizedMessage}")
            }
        }
    }
}