package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataInicioAlumnoN
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsInicioAlumnoModeloN {

    suspend fun obtenerDatosInicio(token: String): Response<dataInicioAlumnoN> { // llama a la api inicio alumno
        return RetrofitCliente.apiAlumnoN.obtenerInicioAlumno("Bearer $token")
    }

    suspend fun listarCitas(token: String): List<CitaDetalleN> { // pide las citas del alumno y regresa la lista
        val respuesta = RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
        val datos = respuesta.body()
        if (respuesta.isSuccessful && datos != null) {
            val citas = datos.data
            if (citas != null) {
                return citas
            }
        }
        return emptyList()
    }

    fun contarCitasProximas(citas: List<CitaDetalleN>): Int { // cuenta las citas que siguen y no estan canceladas
        val hoy = obtenerHoyServidor()
        var total = 0
        for (cita in citas) {
            val fecha = cita.fecha
            if (fecha != null && fecha >= hoy && cita.estado != "Cancelada") {
                total = total + 1
            }
        }
        return total
    }

    fun calcularPendientes(citas: List<CitaDetalleN>): String { // revisa si el alumno tiene citas vencidas o una cita hoy
        val hoy = obtenerHoyServidor()
        var citasVencidas = 0
        var tieneCitaHoy = false
        for (cita in citas) {
            val fecha = cita.fecha
            if (fecha != null && cita.estado == "Pendiente") {
                if (fecha < hoy) {
                    citasVencidas = citasVencidas + 1
                }
                if (fecha == hoy) {
                    tieneCitaHoy = true
                }
            }
        }
        var textoPendientes = "Sin pendientes"
        if (citasVencidas > 0) {
            textoPendientes = "Citas vencidas: " + citasVencidas
        } else if (tieneCitaHoy) {
            textoPendientes = "Tienes cita hoy"
        }
        return textoPendientes
    }

    fun obtenerHoyServidor(): String { // regresa la fecha de hoy igual que la manda el servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(Date())
    }

    fun obtenerFechaHoy(): String { // arma el texto de la fecha de hoy para el saludo
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX"))
        val fecha = formato.format(Date())
        return fecha.substring(0, 1).uppercase() + fecha.substring(1)
    }
}

