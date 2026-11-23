package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsAgendarCitaModeloN
import com.example.servicios_medicos.vista.AgendarCitaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class clsAgendarCitaPresentadorN(private val vista: AgendarCitaActivityN) {
    private val modelo = clsAgendarCitaModeloN()
    private var token = ""
    private var motivoCitaSeleccionado = ""
    private var posicionMotivoCita = 0
    private var fechaSeleccionada = ""
    private var horaSeleccionada = ""
    private var lunesSemana: Calendar = modelo.obtenerLunes(modelo.obtenerHoy())

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra motivos, semana y el dia de hoy
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        seleccionarMotivoCita(0)
        mostrarSemana()
        val hoy = modelo.convertirFechaServidor(modelo.obtenerHoy())
        val dias = modelo.obtenerDiasSemana(lunesSemana)
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == hoy) {
                seleccionarDia(i)
            }
        }
    }
    fun seleccionarMotivoCita(posicion: Int) { // guarda el motivo que eligio el alumno o pide escribirlo si es Otro motivo
        val motivos = modelo.obtenerMotivosCita()
        if (posicion < 0 || posicion >= motivos.size) {
            return
        }
        if (motivos[posicion] == "Otro motivo") {
            vista.pedirOtroMotivoCita()
            return
        }
        posicionMotivoCita = posicion
        motivoCitaSeleccionado = motivos[posicion]
        vista.marcarMotivoCita(posicionMotivoCita)
        actualizarResumen()
    }
    fun escribirOtroMotivoCita(texto: String) { // guarda el motivo que escribio el alumno
        if (texto.trim().isEmpty()) {
            vista.mostrarError("Escribe el motivo de tu cita.")
            vista.marcarMotivoCita(posicionMotivoCita)
            return
        }
        posicionMotivoCita = 3
        motivoCitaSeleccionado = texto.trim()
        vista.marcarMotivoCita(posicionMotivoCita)
        actualizarResumen()
    }
    fun cancelarOtroMotivoCita() { // deja el motivo que estaba antes si el alumno cancela
        vista.marcarMotivoCita(posicionMotivoCita)
    }
    fun semanaAnterior() { // regresa a la semana anterior sin pasar de la semana actual
        val lunesDeHoy = modelo.obtenerLunes(modelo.obtenerHoy())
        val lunesAnterior = modelo.cambiarSemana(lunesSemana, -1)
        if (lunesAnterior.before(lunesDeHoy)) {
            vista.mostrarError("No puedes agendar en fechas pasadas.")
            return
        }
        lunesSemana = lunesAnterior
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
        val lunesDeHoy = modelo.obtenerLunes(modelo.obtenerHoy())
        if (lunesSemana.after(lunesDeHoy)) {
            vista.activarSemanaAnterior(true)
        } else {
            vista.activarSemanaAnterior(false)
        }
        var diaMarcado = -1
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == fechaSeleccionada) {
                diaMarcado = i
            }
        }
        vista.marcarDia(diaMarcado)
    }
    fun seleccionarDia(numeroDia: Int) { // guarda el dia que eligio el alumno y busca sus horarios
        val dias = modelo.obtenerDiasSemana(lunesSemana)
        if (numeroDia < 0 || numeroDia >= dias.size) {
            return
        }
        if (!dias[numeroDia].habilitado) {
            vista.mostrarError("Ese día ya pasó, elige otra fecha.")
            return
        }
        val fecha = modelo.obtenerFechaDelDia(lunesSemana, numeroDia)
        fechaSeleccionada = dias[numeroDia].fechaServidor
        horaSeleccionada = ""
        vista.marcarDia(numeroDia)
        vista.mostrarFechaSeleccionada(modelo.obtenerTextoFechaSeleccionada(fecha))
        vista.mostrarMes(modelo.obtenerTextoMes(fecha))
        actualizarResumen()
        cargarHorarios()
    }
    fun cargarHorarios() { // pide al modelo las horas libres del dia y las manda a la vista
        vista.mostrarHorarios(emptyList())
        if (token.isEmpty() || fechaSeleccionada.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerHorariosDisponibles(token, fechaSeleccionada)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    val horasLibres = modelo.obtenerHorasLibres(datos)
                    vista.mostrarHorarios(horasLibres)
                    if (horasLibres.isEmpty()) {
                        vista.mostrarError("No hay horarios disponibles para este día.")
                    }
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar los horarios. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }
    fun seleccionarHorario(hora: String) { // guarda la hora que eligio el alumno
        horaSeleccionada = hora
        vista.marcarHorario(hora)
        actualizarResumen()
    }
    private fun actualizarResumen() { // pide al modelo el resumen de la cita y lo manda a la vista
        val resumen = modelo.obtenerResumen(motivoCitaSeleccionado, fechaSeleccionada, horaSeleccionada)
        vista.mostrarResumen(resumen[0], resumen[1], resumen[2])
    }
    fun confirmarCita() {  // revisa que los datos esten llenos
        if (token.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        if (motivoCitaSeleccionado.isEmpty()) {
            vista.mostrarError("Selecciona el motivo de tu cita.")
            return
        }
        if (fechaSeleccionada.isEmpty()) {
            vista.mostrarError("Selecciona una fecha.")
            return
        }
        if (horaSeleccionada.isEmpty()) {
            vista.mostrarError("Selecciona un horario.")
            return
        }
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.agendarCita(token, fechaSeleccionada, horaSeleccionada, motivoCitaSeleccionado)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    vista.mostrarCitaAgendada("Tu cita quedó agendada.")
                } else if (respuesta.code() == 409) {
                    vista.mostrarError("Ese horario ya fue ocupado, elige otro.")
                    horaSeleccionada = ""
                    actualizarResumen()
                    cargarHorarios()
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No se pudo agendar tu cita. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
            vista.mostrarCargando(false)
        }
    }
}
