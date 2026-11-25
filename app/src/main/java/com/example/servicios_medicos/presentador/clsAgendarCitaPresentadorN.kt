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
    private var motivoElegido = ""
    private var fechaElegida = ""
    private var textoFecha = ""
    private var horaElegida = ""
    private var horasDelDia: List<String> = emptyList()
    private var doctoraDelDia = "Médico por asignar · Servicio Médico"
    private val lunesDeHoy = modelo.buscarLunesDeLaSemana(Calendar.getInstance())
    private var lunesSemana = lunesDeHoy
    private var numeroSemana = 0
    private val semanasMaximas = 4

    fun iniciarPantalla(tokenSesion: String?, motivoRecibido: String?) { // guarda el token, marca el motivo si viene uno y marca el dia de hoy
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        vista.marcarMotivo(-1)
        val motivos = modelo.listaDeMotivos()
        for (i in 0 until motivos.size) {
            if (motivos[i] == motivoRecibido) {
                elegirMotivo(i)
            }
        }
        actualizarResumen()
        mostrarSemana()
        val hoy = modelo.fechaDeHoy()
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == hoy && dias[i].habilitado) {
                elegirDia(i)
            }
        }
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
            vista.mostrarError("No puedes agendar en fechas pasadas.")
            return
        }
        numeroSemana = numeroSemana - 1
        lunesSemana = modelo.moverSemanas(lunesDeHoy, numeroSemana)
        mostrarSemana()
    }

    fun semanaSiguiente() { // avanza una semana, hasta 4 semanas despues de hoy
        if (numeroSemana == semanasMaximas) {
            vista.mostrarError("Solo puedes agendar con " + semanasMaximas + " semanas de anticipación.")
            return
        }
        numeroSemana = numeroSemana + 1
        lunesSemana = modelo.moverSemanas(lunesDeHoy, numeroSemana)
        mostrarSemana()
    }

    private fun mostrarSemana() { // manda a la vista el mes, los dias y las flechas
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        vista.mostrarMes(modelo.textoDelMes(lunesSemana))
        vista.mostrarSemana(dias)

        if (numeroSemana > 0) {
            vista.activarFlechaAtras(true)
        } else {
            vista.activarFlechaAtras(false)
        }
        if (numeroSemana < semanasMaximas) {
            vista.activarFlechaAdelante(true)
        } else {
            vista.activarFlechaAdelante(false)
        }

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
        if (numeroDia < 0 || numeroDia >= dias.size) {
            return
        }
        if (dias[numeroDia].habilitado == false) {
            vista.mostrarError("Ese día no hay servicio o ya pasó, elige otra fecha.")
            return
        }
        val fecha = modelo.fechaDelDia(lunesSemana, numeroDia)
        fechaElegida = dias[numeroDia].fechaServidor
        textoFecha = modelo.textoFechaCorta(fecha)
        vista.marcarDia(numeroDia)
        vista.mostrarDiaElegido(modelo.textoDiaElegido(fecha))
        cargarHorarios()
    }

    fun cargarHorarios() { // revisa que no tenga cita ese dia y trae las horas libres
        horaElegida = ""
        horasDelDia = emptyList()
        vista.mostrarHorarios(horasDelDia)
        actualizarResumen()
        if (token.isEmpty() || fechaElegida.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val tieneCita = modelo.tieneCitaEseDia(token, fechaElegida)
                val respuesta = modelo.pedirHorarios(token, fechaElegida)
                val datos = respuesta.body()
                if (tieneCita) {
                    vista.mostrarError("Ya tienes una cita ese día. Elige otra fecha.")
                } else if (respuesta.isSuccessful && datos != null && datos.status) {
                    horasDelDia = modelo.sacarHorasLibres(datos, fechaElegida)
                    doctoraDelDia = modelo.textoDoctora(datos)
                    vista.mostrarHorarios(horasDelDia)
                    actualizarResumen()
                    if (horasDelDia.isEmpty()) {
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

    fun elegirHorario(posicion: Int) { // guarda la hora elegida, la posicion 0 es el texto selecciona un horario
        if (posicion == 0 || posicion > horasDelDia.size) {
            horaElegida = ""
        } else {
            horaElegida = horasDelDia[posicion - 1]
        }
        actualizarResumen()
    }

    private fun actualizarResumen() { // manda a la vista el resumen de la cita
        val textoMotivo = modelo.textoResumenMotivo(motivoElegido, textoFecha)
        val textoHorario = modelo.textoResumenHorario(horaElegida)
        vista.mostrarResumen(textoMotivo, textoHorario, doctoraDelDia)
    }

    fun confirmarCita() { // revisa que el alumno eligio todo y guarda la cita
        if (token.isEmpty()) {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
            return
        }
        if (motivoElegido.isEmpty()) {
            vista.mostrarError("Selecciona el motivo de tu cita.")
            return
        }
        if (fechaElegida.isEmpty()) {
            vista.mostrarError("Selecciona una fecha.")
            return
        }
        if (horaElegida.isEmpty()) {
            vista.mostrarError("Selecciona un horario.")
            return
        }
        vista.mostrarCargando(true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.guardarCita(token, fechaElegida, horaElegida, motivoElegido)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    vista.mostrarCitaAgendada("Tu cita quedó agendada.")
                } else if (respuesta.code() == 409) {
                    vista.mostrarError("Ese horario ya fue ocupado, elige otro.")
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
