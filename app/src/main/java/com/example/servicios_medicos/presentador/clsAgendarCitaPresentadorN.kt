package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsAgendarCitaModeloN
import com.example.servicios_medicos.vista.AgendarCitaActivityN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class clsAgendarCitaPresentadorN(private val vista: AgendarCitaActivityN) {
    private val modelo = clsAgendarCitaModeloN()
    private var token = ""
    private var motivoElegido = ""
    private var fechaElegida = ""
    private var fechaCorta = ""
    private var horaElegida = ""
    private var horasDelDia: List<String> = emptyList()
    private var doctora = "Médico por asignar - Servicio Médico"
    private val lunesDeHoy = modelo.buscarLunes(Calendar.getInstance())
    private var lunesSemana = lunesDeHoy
    private var numeroSemana = 0
    private val semanasMaximas = 4

    fun iniciarPantalla(tokenSesion: String?, motivoRecibido: String?) { // guarda el token, marca el motivo si viene uno y elige el dia de hoy
        token = tokenSesion ?: ""
        if (token.isEmpty()) {
            vista.mostrarMensaje("Tu sesión terminó, vuelve a entrar")
        }
        elegirMotivo(modelo.listaDeMotivos().indexOf(motivoRecibido ?: ""))
        mostrarSemana()
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == hoy && dias[i].habilitado) {
                elegirDia(i)
            }
        }
        actualizarResumen()
    }

    fun elegirMotivo(posicion: Int) { // guarda el motivo que eligio el alumno
        val motivos = modelo.listaDeMotivos()
        if (posicion < 0 || posicion >= motivos.size) {
            return
        }
        motivoElegido = motivos[posicion]
        vista.marcarMotivo(posicion)
        actualizarResumen()
    }

    fun semanaAnterior() { // regresa una semana, no deja pasar de la semana de hoy
        if (numeroSemana == 0) {
            vista.mostrarMensaje("No puedes agendar en fechas pasadas")
            return
        }
        numeroSemana = numeroSemana - 1
        lunesSemana = modelo.moverDias(lunesDeHoy, 7 * numeroSemana)
        mostrarSemana()
    }

    fun semanaSiguiente() { // avanza una semana, hasta 4 semanas despues de hoy
        if (numeroSemana == semanasMaximas) {
            vista.mostrarMensaje("Solo puedes agendar hasta " + semanasMaximas + " semanas adelante")
            return
        }
        numeroSemana = numeroSemana + 1
        lunesSemana = modelo.moverDias(lunesDeHoy, 7 * numeroSemana)
        mostrarSemana()
    }

    private fun mostrarSemana() { // manda a la vista el mes, los dias, las flechas y el dia marcado
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        val mes = SimpleDateFormat("MMMM yyyy", Locale("es", "MX")).format(lunesSemana.time)
        vista.mostrarSemana(mes.substring(0, 1).uppercase() + mes.substring(1), dias)
        vista.activarFlechas(numeroSemana > 0, numeroSemana < semanasMaximas)
        var diaMarcado = -1
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == fechaElegida) {
                diaMarcado = i
            }
        }
        vista.marcarDia(diaMarcado)
    }

    fun elegirDia(numeroDia: Int) { // guarda el dia que eligio el alumno y busca sus horarios
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        val fecha = modelo.moverDias(lunesSemana, numeroDia)
        fechaElegida = dias[numeroDia].fechaServidor
        fechaCorta = SimpleDateFormat("EEE d MMM", Locale("es", "MX")).format(fecha.time)
        val diaElegido = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "MX")).format(fecha.time)
        vista.marcarDia(numeroDia)
        vista.mostrarDiaElegido(diaElegido.substring(0, 1).uppercase() + diaElegido.substring(1) + " - Horarios disponibles")
        cargarHorarios()
    }

    fun cargarHorarios() { // revisa que no tenga cita ese dia y trae las horas libres
        horaElegida = ""
        horasDelDia = emptyList()
        vista.mostrarHorarios(listOf("Selecciona un horario"))
        actualizarResumen()
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (modelo.tieneCitaEseDia(token, fechaElegida)) {
                    vista.mostrarMensaje("Ya tienes una cita ese día")
                } else {
                    val horarios = modelo.obtenerHorarios(token, fechaElegida)
                    horasDelDia = modelo.sacarHorasLibres(horarios.horarios, fechaElegida)
                    doctora = (horarios.doctora ?: "Médico por asignar") + " - " + (horarios.consultorio ?: "Servicio Médico")
                    val opciones = mutableListOf<String>()
                    if (horasDelDia.isEmpty()) {
                        opciones.add("Sin horarios disponibles")
                        vista.mostrarMensaje("No hay horarios para este día")
                    } else {
                        opciones.add("Selecciona un horario")
                    }
                    for (hora in horasDelDia) {
                        opciones.add(hora)
                    }
                    vista.mostrarHorarios(opciones)
                    actualizarResumen()
                }
            } catch (e: Exception) {
                vista.mostrarMensaje("Error al cargar los horarios")
            }
        }
    }

    fun elegirHorario(posicion: Int) { // guarda la hora elegida, la posicion 0 es el texto selecciona un horario
        if (posicion == 0 || posicion > horasDelDia.size) {
            horaElegida = ""
        } else {
            horaElegida = horasDelDia[posicion - 1]
        }
        actualizarResumen()
    }

    private fun actualizarResumen() { // arma los textos del resumen y los manda a la vista
        var textoMotivo = "Selecciona un motivo"
        if (motivoElegido.isNotEmpty()) {
            textoMotivo = motivoElegido
        }
        var textoFecha = "Selecciona una fecha"
        if (fechaCorta.isNotEmpty()) {
            textoFecha = fechaCorta
        }
        var textoHorario = "Selecciona un horario"
        if (horaElegida.isNotEmpty()) {
            textoHorario = horaElegida + " a " + modelo.sumarMediaHora(horaElegida)
        }
        vista.mostrarResumen(textoMotivo + " - " + textoFecha, textoHorario, doctora)
    }

    fun confirmarCita() { // revisa que el alumno eligio todo y guarda la cita
        if (token.isEmpty()) {
            vista.mostrarMensaje("Tu sesión terminó, vuelve a entrar")
            return
        }
        if (motivoElegido.isEmpty()) {
            vista.mostrarMensaje("Selecciona un motivo")
            return
        }
        if (fechaElegida.isEmpty()) {
            vista.mostrarMensaje("Selecciona una fecha")
            return
        }
        if (horaElegida.isEmpty()) {
            vista.mostrarMensaje("Selecciona un horario")
            return
        }
        vista.mostrarBotonConfirmar("Agendando...", false)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.guardarCita(token, fechaElegida, horaElegida, motivoElegido)
                if (respuesta.status) {
                    vista.mostrarCitaAgendada("Cita agendada")
                } else {
                    vista.mostrarMensaje("No se pudo agendar la cita")
                }
            } catch (e: Exception) {
                vista.mostrarMensaje("No se pudo agendar, ese horario puede estar ocupado")
            }
            vista.mostrarBotonConfirmar("Confirmar cita", true)
        }
    }
}
