package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsDetalleCitaModeloN
import com.example.servicios_medicos.vista.DetalleCitaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class clsDetalleCitaPresentadorN(private val vista: DetalleCitaActivityN) {
    private val modelo = clsDetalleCitaModeloN()
    private var token = ""
    private var idCita = 0
    private var motivoCita = ""

    fun iniciarPantalla(tokenSesion: String?, idCitaRecibido: Int) { // guarda el token y la cita y carga sus datos
        token = tokenSesion ?: ""
        idCita = idCitaRecibido
        if (token.isEmpty()) {
            vista.mostrarMensaje("Tu sesión terminó, vuelve a entrar")
            return
        }
        if (idCita == 0) {
            vista.mostrarMensaje("No se encontró la cita")
            return
        }
        cargarDetalle()
    }

    fun cargarDetalle() { // pide los datos de la cita y los manda a la vista
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val cita = modelo.obtenerDetalle(token, idCita)
                if (cita == null) {
                    vista.mostrarMensaje("No se encontró la cita")
                } else {
                    motivoCita = cita.motivo ?: ""

                    var estado = cita.estado ?: "Sin estado"
                    if (estado == "Pendiente" && modelo.yaPaso(cita)) {
                        estado = "Vencida"
                    }

                    var fechaLarga = "Sin fecha"
                    val fechaServidor = cita.fecha ?: ""
                    if (fechaServidor.length == 10) {
                        val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(fechaServidor)
                        fechaLarga = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX")).format(fecha)
                        fechaLarga = fechaLarga.substring(0, 1).uppercase() + fechaLarga.substring(1)
                    }

                    var hora = cita.hora ?: ""
                    if (hora.length > 5) {
                        hora = hora.substring(0, 5)
                    }
                    vista.mostrarDetalle(
                        cita.motivo ?: "Cita médica",
                        estado,
                        fechaLarga,
                        hora + " a " + modelo.sumarMediaHora(hora),
                        "Folio de cita: " + (cita.codigoQr ?: cita.idCita.toString()),
                        cita.doctora ?: "Por asignar",
                        cita.consultorio ?: "Por asignar"
                    )

                    if (estado == "Vencida") {
                        vista.mostrarVolverAAgendar("Volver a agendar")
                    } else if (estado == "Pendiente") {
                        vista.mostrarBotonCancelar("Cancelar cita", true)
                    } else {
                        vista.mostrarBotonCancelar("Cancelar cita", false)
                    }
                }
            } catch (e: Exception) {
                vista.mostrarMensaje("Error al cargar la cita")
            }
        }
    }

    fun confirmarCancelacion() { // cancela la cita y vuelve a cargar sus datos
        vista.mostrarBotonCancelar("Cancelando...", false)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.cancelarCita(token, idCita)
                if (respuesta.status) {
                    vista.mostrarMensaje("Cita cancelada")
                } else {
                    vista.mostrarMensaje("No se pudo cancelar la cita")
                }
            } catch (e: Exception) {
                vista.mostrarMensaje("No se pudo cancelar, solo se cancelan citas pendientes")
            }
            vista.mostrarBotonCancelar("Cancelar cita", false)
            cargarDetalle()
        }
    }

    fun reprogramarCita() { // abre agendar cita con el mismo motivo para elegir otro dia
        vista.abrirAgendarCita(token, motivoCita)
    }
}
