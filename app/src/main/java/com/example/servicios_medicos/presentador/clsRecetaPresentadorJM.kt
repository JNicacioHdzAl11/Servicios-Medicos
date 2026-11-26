package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.dataRecetaJM
import com.example.servicios_medicos.modelo.MedicamentoRecetaJM
import com.example.servicios_medicos.modelo.clsRecetaModeloJM
import com.example.servicios_medicos.vista.RecetaActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsRecetaPresentadorJM(private val vista: RecetaActivityJM) {
    private val modelo = clsRecetaModeloJM()
    private var token = ""
    private var idConsulta = 0
    private var medicamentos: MutableList<MedicamentoRecetaJM> = mutableListOf()

    fun iniciarPantalla(
        tokenSesion: String?,
        idConsultaRecibida: Int,
        medicamentoInicial: MedicamentoRecetaJM? = null
    ) {
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        idConsulta = idConsultaRecibida

        if (medicamentoInicial != null && medicamentoInicial.nombre.isNotEmpty()) {
            medicamentos.clear()
            medicamentos.add(medicamentoInicial)
            vista.mostrarMedicamentoActual(medicamentoInicial)
        }
    }

    fun cargarReceta() {
        if (token.isEmpty() || idConsulta <= 0) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerReceta(token, idConsulta)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null) {
                    mostrarDatosReceta(datos)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar la receta. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun filtrarMedicamento(texto: String) {
        val filtrados = modelo.filtrarMedicamentos(medicamentos, texto)
        if (filtrados.isNotEmpty()) {
            vista.mostrarMedicamentoActual(filtrados.first())
        } else if (texto.trim().isNotEmpty()) {
            vista.ocultarTarjetaMedicamento()
        } else if (medicamentos.isNotEmpty()) {
            vista.mostrarMedicamentoActual(medicamentos.last())
        }
    }

    fun agregarMedicamento(nombreBuscado: String) {
        val nombreLimpio = nombreBuscado.trim()
        if (nombreLimpio.isEmpty()) {
            vista.mostrarError("Escribe el nombre del medicamento para agregarlo.")
            return
        }
        val nuevo = MedicamentoRecetaJM(
            nombre = nombreLimpio,
            presentacionVia = "Tabletas · Vía oral",
            dosis = "1 unidad",
            frecuencia = "Cada 8 horas",
            duracion = "Por 5 días",
            nota = "Seguir indicación médica"
        )
        medicamentos.add(nuevo)
        vista.mostrarMedicamentoActual(nuevo)
    }

    fun eliminarUltimoMedicamento() {
        if (medicamentos.isEmpty()) {
            vista.ocultarTarjetaMedicamento()
            return
        }
        medicamentos.removeAt(medicamentos.lastIndex)
        if (medicamentos.isNotEmpty()) {
            vista.mostrarMedicamentoActual(medicamentos.last())
        } else {
            vista.ocultarTarjetaMedicamento()
        }
    }

    fun guardarReceta(indicaciones: String, emitir: Boolean) {
        if (token.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        val errorValidacion = modelo.validarIndicaciones(indicaciones, medicamentos)
        if (errorValidacion != null) {
            vista.mostrarError(errorValidacion)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.guardarReceta(token, idConsulta, medicamentos, indicaciones, emitir)
                if (respuesta.isSuccessful) {                    val mensaje = if (emitir) "Receta emitida correctamente." else "Borrador guardado."
                    vista.mostrarExito(mensaje, cerrarPantalla = emitir)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No se pudo guardar la receta. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarDatosReceta(datos: dataRecetaJM) {
        val detalle = datos.data ?: return
        vista.mostrarEncabezado(
            nombre = detalle.nombrePaciente,
            matriculaCarrera = detalle.matriculaCarreraCuatrimestre,
            fechaMedico = detalle.fechaMedico
        )
        vista.mostrarAlertaAlergia(texto = modelo.formatearAlertaAlergia(detalle.alergiaRegistrada))
        vista.mostrarIndicaciones(detalle.indicaciones)

        if (detalle.listaMedicamentos.isNotEmpty()) {
            medicamentos = detalle.listaMedicamentos.map { item ->
                MedicamentoRecetaJM(
                    nombre = item.nombre,
                    presentacionVia = item.presentacionVia,
                    dosis = item.dosis,
                    frecuencia = item.frecuencia,
                    duracion = item.duracion,
                    nota = item.nota
                )
            }.toMutableList()
            vista.mostrarMedicamentoActual(medicamentos.last())
        }
    }
}