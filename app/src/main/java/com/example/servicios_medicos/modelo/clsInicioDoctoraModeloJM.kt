package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsInicioDoctoraModeloJM {

    suspend fun obtenerInicioDoctora(token: String): Response<dataInicioDoctoraJM> { // llama a la api inicio doctora
        return RetrofitCliente.apiDoctoraJM.obtenerInicioDoctora("Bearer $token")
    }

    fun armarSaludo(nombreCompleto: String): String { // arma el saludo de la doctora
        val nombre = nombreCompleto.trim()
        if (nombre.isEmpty()) {
            return "Buenos días, Doctora"
        }
        return "Buenos días, $nombre"
    }

    fun formatearCedula(cedula: String): String { // arma el texto de cedula profesional
        val valor = cedula.trim()
        if (valor.isEmpty()) {
            return "Cédula profesional no registrada"
        }
        return "Cédula profesional $valor"
    }

    fun formatearContador(cantidad: Int): String { // formatea contadores a dos digitos
        return String.format(Locale.US, "%02d", cantidad)
    }

    fun obtenerInicialesPaciente(nombreEspera: String): String { // obtiene las iniciales del paciente en espera
        val partes = nombreEspera.trim().split(" ").filter { it.isNotEmpty() }
        return when {
            partes.size >= 2 -> (partes[0].take(1) + partes[1].take(1)).uppercase()
            partes.size == 1 -> partes[0].take(2).uppercase()
            else -> "AL"
        }
    }

    fun obtenerFechaHoy(): String { // arma el texto de la fecha de hoy
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX"))
        val fecha = formato.format(Date())
        return fecha.substring(0, 1).uppercase() + fecha.substring(1)
    }
}