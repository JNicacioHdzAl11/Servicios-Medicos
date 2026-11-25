package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.CitaDetalleN
import com.example.servicios_medicos.modelo.clsDetalleCitaModeloN
import com.example.servicios_medicos.vista.DetalleCitaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsDetalleCitaPresentadorN(private val vista: DetalleCitaActivityN) {
    private val modelo = clsDetalleCitaModeloN()
    private var token = ""
    private var idCita = 0
    private var motivoCita = ""

    fun iniciarPantalla(tokenSesion: String?, idCitaRecibido: Int) { // guarda el token y la cita y carga sus datos
        if (tokenSesion == null || tokenSesion.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        if (idCitaRecibido == 0) {
            vista.mostrarError("No encontramos tu cita.")
            return
        }
        token = tokenSesion
        idCita = idCitaRecibido
        cargarDetalle()
    }

    fun cargarDetalle() { // pide los datos de la cita y los manda a la vista
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.pedirDetalle(token, idCita)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status && datos.data != null) {
                    mostrarCita(datos.data)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else if (respuesta.code() == 404) {
                    vista.mostrarError("No encontramos tu cita.")
                } else {
                    vista.mostrarError("No pudimos cargar tu cita. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarCita(cita: CitaDetalleN) { // arma los textos de la cita y elige que botones mostrar
        motivoCita = modelo.textoORespaldo(cita.motivo, "")
        vista.mostrarDetalle(
            modelo.textoORespaldo(cita.motivo, "Cita médica"),
            modelo.estadoParaMostrar(cita),
            modelo.textoFechaLarga(cita.fecha),
            modelo.textoHorario(cita.hora),
            modelo.textoFolio(cita),
            modelo.textoORespaldo(cita.doctora, "Por asignar"),
            modelo.textoORespaldo(cita.consultorio, "Por asignar")
        )
        if (modelo.estadoParaMostrar(cita) == "Vencida") {
            vista.mostrarVolverAAgendar()
        } else {
            vista.activarCancelar(modelo.sePuedeCancelar(cita))
        }
    }

    fun cancelarCita() { // pregunta al alumno si quiere cancelar la cita
        vista.preguntarCancelar()
    }

    fun confirmarCancelacion() { // cancela la cita y vuelve a cargar sus datos
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.pedirCancelar(token, idCita)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    vista.mostrarMensaje("Tu cita quedó cancelada.")
                } else if (respuesta.code() == 400) {
                    vista.mostrarError("Solo puedes cancelar citas pendientes que no han pasado.")
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No se pudo cancelar tu cita. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
            vista.mostrarCargando(false)
            cargarDetalle()
        }
    }

    fun reprogramarCita() { // abre agendar cita con el mismo motivo para elegir otro dia
        vista.abrirAgendarCita(token, motivoCita)
    }
}
