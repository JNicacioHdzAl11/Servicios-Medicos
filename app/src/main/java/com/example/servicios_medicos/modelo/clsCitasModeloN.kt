package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class clsCitasModeloN {

    suspend fun listarCitas(token: String): Response<ListaCitasResponseN> { // llama a la api listar citas
        return RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
    }

    fun obtenerTexto(valor: String?): String { // si el texto viene vacio regresa uno en blanco
        if (valor == null) {
            return ""
        }
        return valor
    }

    fun obtenerCitasProximas(citas: List<CitaDetalleN>): List<CitaDetalleN> { // separa las citas que siguen y no estan canceladas
        val hoy = convertirFechaServidor(obtenerHoy())
        val citasProximas = mutableListOf<CitaDetalleN>()
        var i = citas.size - 1
        while (i >= 0) {
            val fecha = obtenerTexto(citas[i].fecha)
            val estado = obtenerTexto(citas[i].estado)
            if (fecha >= hoy && estado != "Cancelada") {
                citasProximas.add(citas[i])
            }
            i = i - 1
        }
        return citasProximas
    }

    fun obtenerCitasHistorial(citas: List<CitaDetalleN>): List<CitaDetalleN> { // separa las citas que ya pasaron o estan canceladas
        val hoy = convertirFechaServidor(obtenerHoy())
        val citasHistorial = mutableListOf<CitaDetalleN>()
        for (cita in citas) {
            val fecha = obtenerTexto(cita.fecha)
            val estado = obtenerTexto(cita.estado)
            if (fecha < hoy || estado == "Cancelada") {
                citasHistorial.add(cita)
            }
        }
        return citasHistorial
    }

    fun obtenerCitasDelDia(citas: List<CitaDetalleN>, fecha: String): List<CitaDetalleN> { // separa las citas de un solo dia
        val citasDelDia = mutableListOf<CitaDetalleN>()
        for (cita in citas) {
            if (obtenerTexto(cita.fecha) == fecha) {
                citasDelDia.add(cita)
            }
        }
        return citasDelDia
    }

    fun obtenerTextoFechaTarjeta(cita: CitaDetalleN): String { // arma la fecha y hora de la tarjeta
        val dias = listOf("Dom.", "Lun.", "Mar.", "Mié.", "Jue.", "Vie.", "Sáb.")
        val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
        val fecha = convertirTextoAFecha(obtenerTexto(cita.fecha))
        val dia = dias[fecha.get(Calendar.DAY_OF_WEEK) - 1]
        val numero = fecha.get(Calendar.DAY_OF_MONTH)
        val mes = meses[fecha.get(Calendar.MONTH)]
        val hora = cortarHora(obtenerTexto(cita.hora))
        return dia + " " + numero + " " + mes + " · " + hora + "–" + calcularHoraFin(hora)
    }

    fun obtenerTextoMedico(cita: CitaDetalleN): String { // arma el texto del medico y consultorio
        var doctora = obtenerTexto(cita.doctora)
        if (doctora.isEmpty()) {
            doctora = "Médico por asignar"
        }
        val consultorio = obtenerTexto(cita.consultorio)
        if (consultorio.isEmpty()) {
            return doctora
        }
        return doctora + " · " + consultorio
    }

    fun cortarHora(hora: String): String { // quita los segundos de la hora
        if (hora.length >= 5) {
            return hora.substring(0, 5)
        }
        return hora
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
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = obtenerFechaDelDia(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            dias.add(DiaSemanaN(letras[i], numero, convertirFechaServidor(fecha), true))
        }
        return dias
    }

    fun convertirFechaServidor(fecha: Calendar): String { // cambia la fecha como la pide el servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(fecha.time)
    }

    fun convertirTextoAFecha(texto: String): Calendar { // cambia la fecha del servidor a fecha normal
        val fecha = Calendar.getInstance()
        if (texto.isEmpty()) {
            return fecha
        }
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val fechaLeida = formato.parse(texto)
        if (fechaLeida != null) {
            fecha.time = fechaLeida
        }
        return fecha
    }

    fun obtenerTextoMes(fecha: Calendar): String { // arma el texto del mes
        val formato = SimpleDateFormat("MMMM yyyy", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return texto.substring(0, 1).uppercase() + texto.substring(1)
    }
}

