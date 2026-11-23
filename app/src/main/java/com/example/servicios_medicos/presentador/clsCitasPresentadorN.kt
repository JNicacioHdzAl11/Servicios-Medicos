package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.CitaDetalleN
import com.example.servicios_medicos.modelo.clsCitasModeloN
import com.example.servicios_medicos.vista.CitasFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class clsCitasPresentadorN(private val vista: CitasFragmentN) {
    private val modelo = clsCitasModeloN()

    private var token = ""
    private var pestaniaActual = "proximas"
    private var fechaSeleccionada = ""
    private var lunesSemana: Calendar = modelo.obtenerLunes(modelo.obtenerHoy())
    private var listaCitas: List<CitaDetalleN> = emptyList()
    private var citasEnPantalla: List<CitaDetalleN> = emptyList()

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la pestaña y la semana
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        vista.marcarPestania(pestaniaActual)
        mostrarSemana()
    }

    fun cargarCitas() { // pide al modelo la lista de citas del alumno
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.listarCitas(token)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    val citas = datos.data
                    if (citas != null) {
                        listaCitas = citas
                    } else {
                        listaCitas = emptyList()
                    }
                    mostrarCitas()
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar tus citas. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun seleccionarPestania(pestania: String) { // cambia entre citas proximas e historial
        pestaniaActual = pestania
        vista.marcarPestania(pestaniaActual)
        mostrarCitas()
    }

    fun semanaAnterior() { // regresa a la semana anterior
        lunesSemana = modelo.cambiarSemana(lunesSemana, -1)
        mostrarSemana()
    }

    fun semanaSiguiente() { // avanza a la siguiente semana
        lunesSemana = modelo.cambiarSemana(lunesSemana, 1)
        mostrarSemana()
    }

    private fun mostrarSemana() { // manda a la vista el mes y los 7 dias de la semana
        val dias = modelo.obtenerDiasSemana(lunesSemana)
        vista.mostrarMes(modelo.obtenerTextoMes(lunesSemana))
        vista.mostrarSemana(dias)

        var diaMarcado = -1
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == fechaSeleccionada) {
                diaMarcado = i
            }
        }
        vista.marcarDia(diaMarcado)
    }

    fun seleccionarDia(numeroDia: Int) { // muestra las citas del dia que eligio el alumno
        val dias = modelo.obtenerDiasSemana(lunesSemana)
        if (numeroDia < 0 || numeroDia >= dias.size) {
            return
        }
        if (dias[numeroDia].fechaServidor == fechaSeleccionada) {
            fechaSeleccionada = ""
            vista.marcarDia(-1)
        } else {
            fechaSeleccionada = dias[numeroDia].fechaServidor
            vista.marcarDia(numeroDia)
        }
        mostrarCitas()
    }

    private fun mostrarCitas() { // muestra las citas en las tarjetas
        val citasProximas = modelo.obtenerCitasProximas(listaCitas)
        vista.mostrarTextoProximas("Próximas (" + citasProximas.size + ")")

        var citas = citasProximas
        if (pestaniaActual == "historial") {
            citas = modelo.obtenerCitasHistorial(listaCitas)
        }
        if (fechaSeleccionada.isNotEmpty()) {
            citas = modelo.obtenerCitasDelDia(citas, fechaSeleccionada)
        }
        citasEnPantalla = citas

        if (citas.isEmpty()) {
            vista.mostrarTituloSeccion("No tienes citas aquí")
        } else if (pestaniaActual == "historial") {
            vista.mostrarTituloSeccion("Historial de citas")
        } else {
            vista.mostrarTituloSeccion("Citas agendadas")
        }

        for (numeroTarjeta in 1..2) {
            if (citas.size >= numeroTarjeta) {
                val cita = citas[numeroTarjeta - 1]
                vista.mostrarTarjeta(
                    numeroTarjeta,
                    modelo.obtenerTexto(cita.motivo),
                    modelo.obtenerTexto(cita.estado),
                    modelo.obtenerTextoFechaTarjeta(cita),
                    modelo.obtenerTextoMedico(cita)
                )
            } else {
                vista.ocultarTarjeta(numeroTarjeta)
            }
        }
    }

    fun verDetalle(numeroTarjeta: Int) { // abre el detalle de la cita de la tarjeta 1 o 2
        if (citasEnPantalla.size >= numeroTarjeta) {
            val cita = citasEnPantalla[numeroTarjeta - 1]
            vista.abrirDetalle(token, cita.idCita)
        }
    }

    fun solicitarCita() { // abre agendar cita
        vista.abrirAgendarCita(token)
    }
}
