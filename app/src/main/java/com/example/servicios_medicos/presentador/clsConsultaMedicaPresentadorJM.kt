package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataDetalleConsultaJM
import com.example.servicios_medicos.modelo.clsConsultaMedicaModeloJM
import com.example.servicios_medicos.vista.ConsultaMedicaActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsConsultaMedicaPresentadorJM(private val vista: ConsultaMedicaActivityJM) {
    private val modelo = clsConsultaMedicaModeloJM()
    private var token = ""
    private var idConsulta = 0
    private var matricula = ""

    fun iniciarPantalla(tokenSesion: String?, idConsultaRecibida: Int, matriculaAlumno: String?) { // guarda sesion, consulta y matricula
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        idConsulta = idConsultaRecibida
        if (matriculaAlumno != null && matriculaAlumno.isNotEmpty()) {
            matricula = matriculaAlumno
        }
    }

    fun cargarDetalleConsulta() { // pide los datos del alumno, signos vitales y consulta sin congelar la pantalla
        if (token.isEmpty() || (idConsulta <= 0 && matricula.isEmpty())) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDetalleConsulta(token, idConsulta)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    mostrarDatosConsulta(datos)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar los datos de la consulta. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun guardarYGenerarReceta( // valida y guarda la consulta para pasar a la pantalla de receta
        motivo: String,
        padecimiento: String,
        sintomas: String,
        diagnostico: String
    ) {
        if (token.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        val errorValidacion = modelo.validarCamposConsulta(motivo, padecimiento, sintomas, diagnostico)
        if (errorValidacion != null) {
            vista.mostrarError(errorValidacion)
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.guardarConsulta(
                    token = token,
                    idConsulta = idConsulta,
                    matricula = matricula,
                    motivo = motivo.trim(),
                    padecimiento = padecimiento.trim(),
                    sintomas = sintomas.trim(),
                    diagnostico = diagnostico.trim()
                )
                val cuerpo = respuesta.body()
                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status) {
                    val idGenerado = cuerpo.idConsulta ?: idConsulta
                    if (idGenerado > 0) {
                        idConsulta = idGenerado
                    }
                    vista.abrirReceta(idConsulta, matricula)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    val mensajeError = cuerpo?.message?.takeIf { it.isNotEmpty() }
                        ?: "No se pudo guardar la consulta. Intenta de nuevo."
                    vista.mostrarError(mensajeError)
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarDatosConsulta(datos: dataDetalleConsultaJM) { // manda a la vista el resumen, signos vitales y campos clinicos
        val detalle = datos.data ?: return

        if (detalle.idConsulta > 0) {
            idConsulta = detalle.idConsulta
        }
        if (detalle.matricula.isNotEmpty()) {
            matricula = detalle.matricula
        }

        vista.mostrarResumenAlumno(
            detalle.nombreAlumno,
            detalle.detalleAlumno,
            modelo.formatearAlergia(detalle.alergia)
        )

        vista.mostrarSignosVitales(
            registro = detalle.registroSignos,
            presion = modelo.formatearSigno("Presión arterial", detalle.presionArterial, "mmHg"),
            temperatura = modelo.formatearSigno("Temperatura", detalle.temperatura, "°C"),
            frecuencia = modelo.formatearSigno("Frecuencia cardíaca", detalle.frecuenciaCardiaca, "lpm"),
            spo2 = modelo.formatearSigno("SpO₂", detalle.spo2, "%")
        )

        vista.mostrarCamposClinicos(
            motivo = detalle.motivoConsulta,
            padecimiento = detalle.padecimientoActual,
            sintomas = detalle.sintomasManifestados,
            diagnostico = detalle.diagnosticoMedico
        )
    }
}