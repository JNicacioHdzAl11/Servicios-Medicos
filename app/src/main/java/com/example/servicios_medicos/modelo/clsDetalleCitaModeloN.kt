package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsDetalleCitaModeloN {

    suspend fun obtenerDetalle(token: String, idCita: Int): CitaDetalleN? { // pide a la api los datos de una cita
        val datos = RetrofitCliente.apiAlumnoN.obtenerDetalleCita("Bearer $token", idCita)
        return datos.data
    }

    suspend fun cancelarCita(token: String, idCita: Int): CancelarCitaResponseN { // le pide a la api que cancele la cita
        val solicitud = CancelarCitaRequestN(idCita)
        return RetrofitCliente.apiAlumnoN.cancelarCita("Bearer $token", solicitud)
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
}