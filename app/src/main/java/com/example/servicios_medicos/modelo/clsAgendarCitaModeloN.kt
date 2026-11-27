package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class clsAgendarCitaModeloN {

    fun listaDeMotivos(): List<String> { // regresa los motivos que puede elegir el alumno
        return listOf("Consulta general", "Nutrición", "Psicología", "Otro motivo")
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

    fun armarDiasDeLaSemana(lunes: Calendar): List<DiaSemanaN> { // arma los 7 dias, solo se puede de lunes a viernes y desde hoy
        val letras = listOf("L", "M", "M", "J", "V", "S", "D")
        val formatoServidor = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val hoy = formatoServidor.format(Date())
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = moverDias(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            val fechaServidor = formatoServidor.format(fecha.time)
            var sePuede = true
            if (fechaServidor < hoy || i >= 5) {
                sePuede = false
            }
            dias.add(DiaSemanaN(letras[i], numero, fechaServidor, sePuede))
        }
        return dias
    }

    suspend fun tieneCitaEseDia(token: String, fecha: String): Boolean { // revisa si el alumno ya tiene una cita pendiente ese dia
        val datos = RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
        val citas = datos.data ?: emptyList()
        for (cita in citas) {
            if (cita.fecha == fecha && cita.estado == "Pendiente") {
                return true
            }
        }
        return false
    }

    suspend fun obtenerHorarios(token: String, fecha: String): HorariosDataN { // pide a la api los horarios y la doctora de ese dia
        val datos = RetrofitCliente.apiAlumnoN.obtenerHorariosDisponibles("Bearer $token", fecha)
        return datos.data ?: HorariosDataN()
    }

    fun sacarHorasLibres(horarios: List<String>, fecha: String): List<String> { // deja las horas libres sin segundos y sin las que ya pasaron hoy
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val ahorita = SimpleDateFormat("HH:mm", Locale.US).format(Date())
        val horasLibres = mutableListOf<String>()
        for (hora in horarios) {
            var horaCorta = hora
            if (horaCorta.length > 5) {
                horaCorta = hora.substring(0, 5)
            }
            if (fecha != hoy || horaCorta > ahorita) {
                horasLibres.add(horaCorta)
            }
        }
        return horasLibres
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

    suspend fun guardarCita(token: String, fecha: String, hora: String, motivo: String): AgendarCitaResponseN { // manda la cita a la api para guardarla
        val solicitud = AgendarCitaRequestN(fecha, hora, motivo)
        return RetrofitCliente.apiAlumnoN.agendarCita("Bearer $token", solicitud)
    }
}
