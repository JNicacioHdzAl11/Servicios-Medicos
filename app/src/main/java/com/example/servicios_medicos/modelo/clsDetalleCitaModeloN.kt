package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class clsDetalleCitaModeloN {
    suspend fun obtenerDetalleCita(token: String, idCita: Int): Response<DetalleCitaResponseN> { // llama a la api detalle cita
        return RetrofitCliente.apiAlumnoN.obtenerDetalleCita("Bearer $token", idCita)
    }
    suspend fun cancelarCita(token: String, idCita: Int): Response<CancelarCitaResponseN> { // llama a la api cancelar cita
        val solicitud = CancelarCitaRequestN(idCita)
        return RetrofitCliente.apiAlumnoN.cancelarCita("Bearer $token", solicitud)
    }
    fun obtenerTexto(valor: String?, respaldo: String): String { // si el texto viene vacio regresa el de respaldo
        if (valor == null || valor.isEmpty()) {
            return respaldo
        }
        return valor
    }
    fun obtenerTextoFecha(fecha: String?): String { // arma el texto de la fecha de la cita
        val texto = obtenerTexto(fecha, "")
        if (texto.isEmpty()) {
            return "Sin fecha"
        }
        val formatoServidor = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val fechaLeida = formatoServidor.parse(texto)
        if (fechaLeida == null) {
            return texto
        }
        val calendario = Calendar.getInstance()
        calendario.time = fechaLeida
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX"))
        return ponerMayuscula(formato.format(calendario.time))
    }
    fun obtenerTextoHorario(hora: String?): String { // arma la hora de inicio y fin de la cita
        val texto = obtenerTexto(hora, "")
        if (texto.length < 5) {
            return "Sin horario"
        }
        val horaInicio = texto.substring(0, 5)
        return horaInicio + "–" + calcularHoraFin(horaInicio)
    }
    fun calcularHoraFin(hora: String): String { // suma 30 minutos a la hora de la cita
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
    fun obtenerTextoFolio(cita: CitaDetalleN): String { // arma el texto del folio de la cita
        val codigo = obtenerTexto(cita.codigoQr, "")
        if (codigo.isEmpty()) {
            return "Folio de cita · " + cita.idCita
        }
        return "Folio de cita · " + codigo
    }
    fun sePuedeCancelar(estado: String?): Boolean { // revisa si la cita esta pendiente para poder cancelarla
        if (estado == "Pendiente") {
            return true
        }
        return false
    }
    private fun ponerMayuscula(texto: String): String { // pone en mayuscula la primera letra
        if (texto.isEmpty()) {
            return texto
        }
        return texto.substring(0, 1).uppercase() + texto.substring(1)
    }
}

