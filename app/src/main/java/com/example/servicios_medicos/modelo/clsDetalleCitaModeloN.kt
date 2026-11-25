package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsDetalleCitaModeloN {

    suspend fun pedirDetalle(token: String, idCita: Int): Response<DetalleCitaResponseN> { // llama a la api detalle cita
        return RetrofitCliente.apiAlumnoN.obtenerDetalleCita("Bearer $token", idCita)
    }

    suspend fun pedirCancelar(token: String, idCita: Int): Response<CancelarCitaResponseN> { // llama a la api cancelar cita
        val solicitud = CancelarCitaRequestN(idCita)
        return RetrofitCliente.apiAlumnoN.cancelarCita("Bearer $token", solicitud)
    }

    fun textoORespaldo(valor: String?, respaldo: String): String { // si el texto no viene regresa el de respaldo
        if (valor == null || valor.isEmpty()) {
            return respaldo
        }
        return valor
    }

    fun yaPaso(cita: CitaDetalleN): Boolean { // revisa si la fecha y hora de la cita ya pasaron
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        val ahora = formato.format(Date())
        val hora = textoORespaldo(cita.hora, "")
        var horaCorta = hora
        if (hora.length >= 5) {
            horaCorta = hora.substring(0, 5)
        }
        val fechaHora = textoORespaldo(cita.fecha, "") + " " + horaCorta
        if (fechaHora < ahora) {
            return true
        }
        return false
    }

    fun estadoParaMostrar(cita: CitaDetalleN): String { // si la cita sigue pendiente pero ya paso dice vencida
        val estado = textoORespaldo(cita.estado, "Sin estado")
        if (estado == "Pendiente" && yaPaso(cita)) {
            return "Vencida"
        }
        return estado
    }

    fun sePuedeCancelar(cita: CitaDetalleN): Boolean { // solo se cancela si esta pendiente y no ha pasado
        if (cita.estado == "Pendiente" && yaPaso(cita) == false) {
            return true
        }
        return false
    }

    fun textoFechaLarga(fecha: String?): String { // arma la fecha con el dia y el mes en letras
        val texto = textoORespaldo(fecha, "")
        if (texto.isEmpty()) {
            return "Sin fecha"
        }
        val formatoServidor = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val fechaLeida = formatoServidor.parse(texto)
        if (fechaLeida == null) {
            return texto
        }
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX"))
        val resultado = formato.format(fechaLeida)
        return resultado.substring(0, 1).uppercase() + resultado.substring(1)
    }

    fun textoHorario(hora: String?): String { // arma la hora de inicio y fin de la cita
        val texto = textoORespaldo(hora, "")
        if (texto.length < 5) {
            return "Sin horario"
        }
        val horaInicio = texto.substring(0, 5)
        return horaInicio + "–" + sumarMediaHora(horaInicio)
    }

    fun sumarMediaHora(hora: String): String { // suma 30 minutos a la hora de la cita
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

    fun textoFolio(cita: CitaDetalleN): String { // arma el texto del folio de la cita
        val codigo = textoORespaldo(cita.codigoQr, "")
        if (codigo.isEmpty()) {
            return "Folio de cita · " + cita.idCita
        }
        return "Folio de cita · " + codigo
    }
}
