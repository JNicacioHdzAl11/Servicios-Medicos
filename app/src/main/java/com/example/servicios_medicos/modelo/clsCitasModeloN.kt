package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class clsCitasModeloN {

    suspend fun obtenerCitas(token: String): List<CitaDetalleN> { // pide a la api las citas del alumno y regresa la lista
        val datos = RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
        return datos.data ?: emptyList()
    }

    fun yaPaso(cita: CitaDetalleN): Boolean { // revisa si la fecha y hora de la cita ya pasaron
        val ahora = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
        var hora = cita.hora ?: ""
        if (hora.length > 5) {
            hora = hora.substring(0, 5)
        }
        val fechaHora = (cita.fecha ?: "") + " " + hora
        return fechaHora < ahora
    }

    fun citasProximas(citas: List<CitaDetalleN>): List<CitaDetalleN> { // junta las citas que no han pasado y no estan canceladas
        val proximas = mutableListOf<CitaDetalleN>()
        for (cita in citas) {
            if (cita.estado != "Cancelada" && yaPaso(cita) == false) {
                proximas.add(0, cita)
            }
        }
        return proximas
    }

    fun citasDelHistorial(citas: List<CitaDetalleN>): List<CitaDetalleN> { // junta las citas que ya pasaron o estan canceladas
        val historial = mutableListOf<CitaDetalleN>()
        for (cita in citas) {
            if (cita.estado == "Cancelada" || yaPaso(cita)) {
                historial.add(cita)
            }
        }
        return historial
    }

    fun citasDelDia(citas: List<CitaDetalleN>, fecha: String): List<CitaDetalleN> { // junta las citas de un solo dia
        val delDia = mutableListOf<CitaDetalleN>()
        for (cita in citas) {
            if (cita.fecha == fecha) {
                delDia.add(cita)
            }
        }
        return delDia
    }

    fun sumarMediaHora(hora: String): String { // suma 30 minutos a la hora de la cita
        if (hora.length < 5) {
            return hora
        }
        if (hora.substring(3, 5) == "00") {
            return hora.substring(0, 3) + "30"
        }
        var horas = (hora.substring(0, 2).toInt() + 1).toString()
        if (horas.length == 1) {
            horas = "0" + horas
        }
        return horas + ":00"
    }

    fun buscarLunes(fecha: Calendar): Calendar { // va hacia atras hasta encontrar el lunes de la semana
        val lunes = Calendar.getInstance()
        lunes.time = fecha.time
        while (lunes.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            lunes.add(Calendar.DAY_OF_MONTH, -1)
        }
        return lunes
    }

    fun moverDias(fecha: Calendar, dias: Int): Calendar { // suma los dias que se le pidan a la fecha
        val nuevaFecha = Calendar.getInstance()
        nuevaFecha.time = fecha.time
        nuevaFecha.add(Calendar.DAY_OF_MONTH, dias)
        return nuevaFecha
    }

    fun armarDiasDeLaSemana(lunes: Calendar): List<DiaSemanaN> { // arma los 7 dias del calendario
        val letras = listOf("L", "M", "M", "J", "V", "S", "D")
        val formatoServidor = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = moverDias(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            dias.add(DiaSemanaN(letras[i], numero, formatoServidor.format(fecha.time), true))
        }
        return dias
    }
}
