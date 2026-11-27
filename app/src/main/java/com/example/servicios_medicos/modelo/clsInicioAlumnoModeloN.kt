package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataInicioAlumnoN
import com.example.servicios_medicos.network.RetrofitCliente
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsInicioAlumnoModeloN {

    suspend fun obtenerDatosInicio(token: String): dataInicioAlumnoN { // pide a la api los datos de inicio del alumno
        return RetrofitCliente.apiAlumnoN.obtenerInicioAlumno("Bearer $token")
    }

    suspend fun obtenerCitas(token: String): List<CitaDetalleN> { // pide a la api las citas del alumno
        val datos = RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
        return datos.data ?: emptyList()
    }

    fun yaPaso(fecha: String, hora: String): Boolean { // revisa si la fecha y hora de la cita ya pasaron
        val ahora = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
        var horaCorta = hora
        if (horaCorta.length > 5) {
            horaCorta = hora.substring(0, 5)
        }
        return fecha + " " + horaCorta < ahora
    }

    fun contarCitasProximas(citas: List<CitaDetalleN>): Int { // cuenta las citas que no han pasado y no estan canceladas
        var total = 0
        for (cita in citas) {
            if (cita.estado != "Cancelada" && cita.estado != "Completada" && yaPaso(cita.fecha ?: "", cita.hora ?: "") == false) {
                total = total + 1
            }
        }
        return total
    }
    fun buscarProximaCita(citas: List<CitaDetalleN>): CitaDetalleN? { // busca la cita pendiente mas cercana que todavia no pasa
        var proxima: CitaDetalleN? = null
        for (cita in citas) {
            if (cita.estado != "Cancelada" && cita.estado != "Completada" && yaPaso(cita.fecha ?: "", cita.hora ?: "") == false) {
                if (proxima == null) {
                    proxima = cita
                } else if ((cita.fecha ?: "") + (cita.hora ?: "") < (proxima.fecha ?: "") + (proxima.hora ?: "")) {
                    proxima = cita
                }
            }
        }
        return proxima
    }

    fun calcularPendientes(citas: List<CitaDetalleN>): String { // revisa si el alumno tiene citas vencidas o una cita hoy
        val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        var citasVencidas = 0
        var tieneCitaHoy = false
        for (cita in citas) {
            if (cita.estado == "Pendiente") {
                if (yaPaso(cita.fecha ?: "", cita.hora ?: "")) {
                    citasVencidas = citasVencidas + 1
                } else if (cita.fecha == hoy) {
                    tieneCitaHoy = true
                }
            }
        }
        if (citasVencidas > 0) {
            return "Citas vencidas: " + citasVencidas
        }
        if (tieneCitaHoy) {
            return "Tienes cita hoy"
        }
        return "Sin pendientes"
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
