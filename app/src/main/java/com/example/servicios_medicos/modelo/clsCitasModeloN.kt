package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class clsCitasModeloN {

    suspend fun pedirCitas(token: String): Response<ListaCitasResponseN> { // llama a la api listar citas
        return RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
    }

    fun textoOVacio(valor: String?): String { // si el texto no viene regresa uno en blanco
        if (valor == null) {
            return ""
        }
        return valor
    }

    fun quitarSegundos(hora: String): String { // deja la hora sin segundos
        if (hora.length >= 5) {
            return hora.substring(0, 5)
        }
        return hora
    }

    fun yaPaso(cita: CitaDetalleN): Boolean { // revisa si la fecha y hora de la cita ya pasaron
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val ahora = formato.format(Date())
        val fechaHora = textoOVacio(cita.fecha) + " " + quitarSegundos(textoOVacio(cita.hora))
        if (fechaHora < ahora) {
            return true
        }
        return false
    }

    fun estadoParaMostrar(cita: CitaDetalleN): String { // si la cita sigue pendiente pero ya paso dice vencida
        val estado = textoOVacio(cita.estado)
        if (estado == "Pendiente" && yaPaso(cita)) {
            return "Vencida"
        }
        return estado
    }

    fun citasProximas(citas: List<CitaDetalleN>): List<CitaDetalleN> { // junta las citas que no han pasado y no estan canceladas
        val proximas = mutableListOf<CitaDetalleN>()
        var i = citas.size - 1
        while (i >= 0) {
            if (citas[i].estado != "Cancelada" && yaPaso(citas[i]) == false) {
                proximas.add(citas[i])
            }
            i = i - 1
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

    fun textoFechaTarjeta(cita: CitaDetalleN): String { // arma el dia, la fecha y la hora de la tarjeta
        val dias = listOf("Dom.", "Lun.", "Mar.", "Mié.", "Jue.", "Vie.", "Sáb.")
        val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
        val fecha = textoAFecha(textoOVacio(cita.fecha))
        val dia = dias[fecha.get(Calendar.DAY_OF_WEEK) - 1]
        val numero = fecha.get(Calendar.DAY_OF_MONTH)
        val mes = meses[fecha.get(Calendar.MONTH)]
        val hora = quitarSegundos(textoOVacio(cita.hora))
        return dia + " " + numero + " " + mes + " · " + hora + "–" + sumarMediaHora(hora)
    }

    fun textoDoctora(cita: CitaDetalleN): String { // arma el nombre de la doctora y su consultorio
        var doctora = textoOVacio(cita.doctora)
        if (doctora.isEmpty()) {
            doctora = "Médico por asignar"
        }
        val consultorio = textoOVacio(cita.consultorio)
        if (consultorio.isEmpty()) {
            return doctora
        }
        return doctora + " · " + consultorio
    }

    fun sumarMediaHora(hora: String): String { // suma 30 minutos a la hora de la cita
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

    fun buscarLunesDeLaSemana(fecha: Calendar): Calendar { // va hacia atras hasta encontrar el lunes
        val lunes = Calendar.getInstance()
        lunes.time = fecha.time
        while (lunes.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            lunes.add(Calendar.DAY_OF_MONTH, -1)
        }
        return lunes
    }

    fun armarDiasDeLaSemana(lunes: Calendar): List<DiaSemanaN> { // arma los 7 dias del calendario
        val letras = listOf("L", "M", "M", "J", "V", "S", "D")
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = moverDias(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            dias.add(DiaSemanaN(letras[i], numero, fechaParaServidor(fecha), true))
        }
        return dias
    }

    fun moverDias(fecha: Calendar, dias: Int): Calendar { // suma los dias que se le pidan a la fecha
        val nuevaFecha = Calendar.getInstance()
        nuevaFecha.time = fecha.time
        nuevaFecha.add(Calendar.DAY_OF_MONTH, dias)
        return nuevaFecha
    }

    fun fechaParaServidor(fecha: Calendar): String { // cambia la fecha como la usa el servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(fecha.time)
    }

    fun textoAFecha(texto: String): Calendar { // cambia la fecha del servidor a fecha de calendario
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

    fun textoDelMes(fecha: Calendar): String { // arma el nombre del mes y el año
        val formato = SimpleDateFormat("MMMM yyyy", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return texto.substring(0, 1).uppercase() + texto.substring(1)
    }
}
