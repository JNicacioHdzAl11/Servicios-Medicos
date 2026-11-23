package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsInicioAlumnoModeloN {
    suspend fun obtenerDatosInicio(token: String): Response<dataInicioAlumnoN> { // llama a la api inicio alumno
        return RetrofitCliente.apiAlumnoN.obtenerInicioAlumno("Bearer $token")
    }
    fun obtenerInfoAlumno(datos: dataInicioAlumnoN): List<String> { // saca el nombre y su matricula
        val info = mutableListOf<String>()
        val contenido = datos.data
        if (contenido != null) {
            val alumno = contenido.alumno
            if (alumno != null) {
                info.add(alumno.nombreCompleto)
                info.add(alumno.matricula)
            }
        }
        return info
    }
    suspend fun calcularTotalCitas(token: String): Int { // cuenta las citas proximas con la api listar citas
        val modeloCitas = clsCitasModeloN()
        val respuesta = modeloCitas.listarCitas(token)
        if (respuesta.isSuccessful) {
            val datos = respuesta.body()
            if (datos != null) {
                val citas = datos.data
                if (citas != null) {
                    val citasProximas = modeloCitas.obtenerCitasProximas(citas)
                    return citasProximas.size
                }
            }
        }
        return 0
    }
    fun obtenerProximaCita(datos: dataInicioAlumnoN): List<String> { // arma los textos de la proxima cita
        val resultado = mutableListOf<String>()
        val contenido = datos.data
        if (contenido != null) {
            val cita = contenido.proximaCita
            if (cita != null) {
                resultado.add(cita.motivo)
                resultado.add(cita.estado)
                resultado.add("Fecha: " + cita.fecha)
                resultado.add("Hora: " + cita.hora)
                return resultado
            }
        }
        resultado.add("Sin citas programadas")
        resultado.add("Sin cita")
        resultado.add("Sin fecha")
        resultado.add("Sin horario asignado")
        return resultado
    }
    fun obtenerCampania1(datos: dataInicioAlumnoN): List<String> { // arma el titulo y detalle de la 1ra campaña
        val campania = mutableListOf<String>()
        val contenido = datos.data
        if (contenido != null) {
            if (contenido.campaniasActivas.size >= 1) {
                val campania1 = contenido.campaniasActivas[0]
                val detalle = "Lugar: " + campania1.lugar + " (" + campania1.fechaInicio + " - " + campania1.fechaFin + ")"
                campania.add(campania1.titulo)
                campania.add(detalle)
                return campania
            }
        }
        campania.add("Sin campañas")
        campania.add("No hay campañas disponibles")
        return campania
    }
    fun obtenerCampania2(datos: dataInicioAlumnoN): List<String> { // arma el titulo y detalle de la 2da campaña
        val campania = mutableListOf<String>()
        val contenido = datos.data
        if (contenido != null) {
            if (contenido.campaniasActivas.size >= 2) {
                val campania2 = contenido.campaniasActivas[1]
                val detalle = "Lugar: " + campania2.lugar + " (" + campania2.fechaInicio + " - " + campania2.fechaFin + ")"
                campania.add(campania2.titulo)
                campania.add(detalle)
                return campania
            }
        }
        campania.add("Sin campañas")
        campania.add("No hay más campañas por ahora")
        return campania
    }
}
