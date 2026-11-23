package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class clsAgendarCitaModeloN {
    suspend fun obtenerHorariosDisponibles(token: String, fecha: String): Response<HorariosDisponiblesResponseN> { // llama a la api horarios disponibles
        return RetrofitCliente.apiAlumnoN.obtenerHorariosDisponibles("Bearer $token", fecha)
    }
    suspend fun agendarCita(token: String, fecha: String, hora: String, motivo: String): Response<AgendarCitaResponseN> { // llama a la api agendar cita
        val solicitud = AgendarCitaRequestN(fecha, hora, motivo)
        return RetrofitCliente.apiAlumnoN.agendarCita("Bearer $token", solicitud)
    }
    fun obtenerMotivosCita(): List<String> { // regresa la lista de motivos de la cita
        return listOf("Consulta general", "Nutrición", "Psicología", "Otro motivo")
    }
    fun obtenerHorasLibres(datos: HorariosDisponiblesResponseN): List<String> { // saca las horas libres y las deja como 09:00
        val horasLibres = mutableListOf<String>()
        val contenido = datos.data
        if (contenido != null) {
            for (hora in contenido.horarios) {
                if (hora.length >= 5) {
                    horasLibres.add(hora.substring(0, 5))
                }
            }
        }
        return horasLibres
    }
    fun obtenerHoy(): Calendar { // regresa la fecha de hoy sin hora
        val hoy = Calendar.getInstance()
        hoy.set(Calendar.HOUR_OF_DAY, 0)
        hoy.set(Calendar.MINUTE, 0)
        hoy.set(Calendar.SECOND, 0)
        hoy.set(Calendar.MILLISECOND, 0)
        return hoy
    }
    fun obtenerLunes(fecha: Calendar): Calendar { // busca el lunes de la semana
        val lunes = Calendar.getInstance()
        lunes.time = fecha.time
        while (lunes.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            lunes.add(Calendar.DAY_OF_MONTH, -1)
        }
        return lunes
    }
    fun cambiarSemana(lunes: Calendar, cantidad: Int): Calendar { // avanza o regresa una semana
        val nuevoLunes = Calendar.getInstance()
        nuevoLunes.time = lunes.time
        nuevoLunes.add(Calendar.DAY_OF_MONTH, 7 * cantidad)
        return nuevoLunes
    }
    fun obtenerFechaDelDia(lunes: Calendar, numeroDia: Int): Calendar { // regresa la fecha de un dia de la semana
        val fecha = Calendar.getInstance()
        fecha.time = lunes.time
        fecha.add(Calendar.DAY_OF_MONTH, numeroDia)
        return fecha
    }
    fun obtenerDiasSemana(lunes: Calendar): List<DiaSemanaN> { // arma los 7 dias de la semana del calendario
        val letras = listOf("L", "M", "M", "J", "V", "S", "D")
        val hoy = obtenerHoy()
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = obtenerFechaDelDia(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            val fechaServidor = convertirFechaServidor(fecha)
            var habilitado = true
            if (fecha.before(hoy)) {
                habilitado = false
            }
            dias.add(DiaSemanaN(letras[i], numero, fechaServidor, habilitado))
        }
        return dias
    }
    fun convertirFechaServidor(fecha: Calendar): String { // convierte la fecha al formato del servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(fecha.time)
    }
    fun convertirTextoAFecha(texto: String): Calendar { // convierte el texto  a fecha
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val fecha = Calendar.getInstance()
        val fechaLeida = formato.parse(texto)
        if (fechaLeida != null) {
            fecha.time = fechaLeida
        }
        return fecha
    }
    fun obtenerTextoMes(fecha: Calendar): String { // arma el texto del mes
        val formato = SimpleDateFormat("MMMM yyyy", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return ponerMayuscula(texto)
    }
    fun obtenerTextoFechaSeleccionada(fecha: Calendar): String { // arma el texto del dia
        val formato = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return ponerMayuscula(texto) + " · Horarios disponibles"
    }
    fun obtenerTextoFechaCorta(fecha: Calendar): String { // arma la fecha corta como 12 nov 2026
        val formato = SimpleDateFormat("d MMM yyyy", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return texto.replace(".", "")
    }
    fun calcularHoraFin(hora: String): String { // suma 30 minutos a la hora de la cita
        if (hora.length < 5) {
            return hora
        }
        var horas = hora.substring(0, 2).toInt()
        var minutos = hora.substring(3, 5).toInt() + 30
        if (minutos >= 60) {
            minutos = minutos - 60
            horas = horas + 1
        }
        var textoHoras = horas.toString()
        if (horas < 10) {
            textoHoras = "0" + horas
        }
        var textoMinutos = minutos.toString()
        if (minutos < 10) {
            textoMinutos = "0" + minutos
        }
        return textoHoras + ":" + textoMinutos
    }
    fun obtenerResumen(motivo: String, fecha: String, hora: String): List<String> { // arma los 3 textos del resumen de la cita
        val resumen = mutableListOf<String>()
        if (fecha.isNotEmpty()) {
            val fechaCalendario = convertirTextoAFecha(fecha)
            resumen.add(motivo + " · " + obtenerTextoFechaCorta(fechaCalendario))
        } else {
            resumen.add(motivo + " · Selecciona una fecha")
        }
        if (hora.isNotEmpty()) {
            resumen.add(hora + "–" + calcularHoraFin(hora) + " · Servicios Médicos")
        } else {
            resumen.add("Selecciona un horario")
        }
        resumen.add("Médico por asignar · Campus")
        return resumen
    }
    private fun ponerMayuscula(texto: String): String { // pone en mayuscula la primera letra
        if (texto.isEmpty()) {
            return texto
        }
        return texto.substring(0, 1).uppercase() + texto.substring(1)
    }
}
