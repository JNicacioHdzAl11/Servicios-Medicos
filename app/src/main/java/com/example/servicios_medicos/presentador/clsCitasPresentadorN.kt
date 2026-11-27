package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.CitaDetalleN
import com.example.servicios_medicos.modelo.TarjetaCitaN
import com.example.servicios_medicos.modelo.clsCitasModeloN
import com.example.servicios_medicos.vista.CitasFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class clsCitasPresentadorN(private val vista: CitasFragmentN) {
    private val modelo = clsCitasModeloN()
    private var token = ""
    private var pestania = "proximas"
    private var fechaElegida = ""
    private var lunesSemana = modelo.buscarLunes(Calendar.getInstance())
    private var todasLasCitas: List<CitaDetalleN> = emptyList()
    private var citasEnPantalla: List<CitaDetalleN> = emptyList()

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la semana de hoy
        token = tokenSesion ?: ""
        if (token.isEmpty()) {
            vista.mostrarMensaje("Tu sesión terminó, vuelve a entrar")
        }
        mostrarSemana()
    }

    fun cargarCitas() { // pide las citas del alumno y las muestra
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                todasLasCitas = modelo.obtenerCitas(token)
                mostrarCitas()
            } catch (e: Exception) {
                vista.mostrarMensaje("Error al cargar las citas")
            }
        }
    }

    fun elegirPestania(pestaniaElegida: String) { // cambia entre proximas e historial
        pestania = pestaniaElegida
        mostrarCitas()
    }

    fun semanaAnterior() { // regresa una semana
        lunesSemana = modelo.moverDias(lunesSemana, -7)
        mostrarSemana()
    }

    fun semanaSiguiente() { // avanza una semana
        lunesSemana = modelo.moverDias(lunesSemana, 7)
        mostrarSemana()
    }

    private fun mostrarSemana() { // manda a la vista el mes, los 7 dias y el dia marcado
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        val mes = SimpleDateFormat("MMMM yyyy", Locale("es", "MX")).format(lunesSemana.time)
        vista.mostrarSemana(mes.substring(0, 1).uppercase() + mes.substring(1), dias)
        var diaMarcado = -1
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == fechaElegida) {
                diaMarcado = i
            }
        }
        vista.marcarDia(diaMarcado)
    }

    fun elegirDia(numeroDia: Int) { // muestra solo las citas del dia elegido, si lo toca otra vez se quita
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        if (dias[numeroDia].fechaServidor == fechaElegida) {
            fechaElegida = ""
            vista.marcarDia(-1)
        } else {
            fechaElegida = dias[numeroDia].fechaServidor
            vista.marcarDia(numeroDia)
        }
        mostrarCitas()
    }

    private fun mostrarCitas() { // elige las citas de la pestaña y el dia, arma sus tarjetas y las manda a la vista
        val proximas = modelo.citasProximas(todasLasCitas)
        var citas = proximas
        var titulo = "Citas agendadas"
        if (pestania == "historial") {
            citas = modelo.citasDelHistorial(todasLasCitas)
            titulo = "Historial de citas"
        }
        if (fechaElegida.isNotEmpty()) {
            citas = modelo.citasDelDia(citas, fechaElegida)
        }
        if (citas.isEmpty()) {
            titulo = "No tienes citas aquí"
        }
        citasEnPantalla = citas
        val tarjetas = mutableListOf<TarjetaCitaN>()
        for (cita in citas) {
            var estado = cita.estado ?: ""
            if (estado == "Pendiente" && modelo.yaPaso(cita)) {
                estado = "Vencida"
            }
            var fecha = cita.fecha ?: ""
            if (fecha.length == 10) {
                val fechaServidor = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(fecha)
                fecha = SimpleDateFormat("EEE d MMM", Locale("es", "MX")).format(fechaServidor)
            }
            var hora = cita.hora ?: ""
            if (hora.length > 5) {
                hora = hora.substring(0, 5)
            }
            val doctora = (cita.doctora ?: "Médico por asignar") + " - " + (cita.consultorio ?: "Servicio Médico")
            tarjetas.add(TarjetaCitaN(cita.motivo ?: "", estado, fecha + " - " + hora + " a " + modelo.sumarMediaHora(hora), doctora))
        }
        vista.mostrarCitas("Próximas (" + proximas.size + ")", titulo, tarjetas)
    }

    fun verDetalle(posicion: Int) { // abre el detalle de la cita que toco el alumno
        if (posicion >= 0 && posicion < citasEnPantalla.size) {
            vista.abrirDetalle(token, citasEnPantalla[posicion].idCita)
        }
    }

    fun solicitarCita() { // abre agendar cita
        vista.abrirAgendarCita(token)
    }
    fun verConfiguracion() { // abre configuracion
        vista.abrirConfiguracion()
    }
}
