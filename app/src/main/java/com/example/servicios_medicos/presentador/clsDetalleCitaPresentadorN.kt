package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsDetalleCitaModeloN
import com.example.servicios_medicos.vista.DetalleCitaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsDetalleCitaPresentadorN(private val vista: DetalleCitaActivityN) {
    private val modelo = clsDetalleCitaModeloN()
    private var token = ""
    private var idCita = 0

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
    fun cargarDetalle() { // pide al modelo los datos de la cita y los manda a la vista
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDetalleCita(token, idCita)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    val cita = datos.data
                    if (cita != null) {
                        vista.mostrarDetalle(
                            modelo.obtenerTexto(cita.motivo, "Cita médica"),
                            modelo.obtenerTexto(cita.estado, "Sin estado"),
                            modelo.obtenerTextoFecha(cita.fecha),
                            modelo.obtenerTextoHorario(cita.hora),
                            modelo.obtenerTextoFolio(cita),
                            modelo.obtenerTexto(cita.doctora, "Por asignar"),
                            modelo.obtenerTexto(cita.consultorio, "Por asignar")
                        )
                        vista.activarCancelar(modelo.sePuedeCancelar(cita.estado))
                    } else {
                        vista.mostrarError("No encontramos tu cita.")
                    }
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
    fun cancelarCita() { // pregunta al alumno si quiere cancelar la cita
        vista.preguntarCancelar()
    }
    fun confirmarCancelacion() { // cancela la cita y vuelve a cargar sus datos
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.cancelarCita(token, idCita)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    vista.mostrarMensaje("Tu cita quedó cancelada.")
                } else if (respuesta.code() == 400) {
                    vista.mostrarError("Solo puedes cancelar citas pendientes.")
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
    fun reprogramarCita() { // abre agendar cita para elegir otro dia
        vista.abrirAgendarCita(token)
    }
}


