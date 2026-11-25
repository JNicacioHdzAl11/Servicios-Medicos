package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataExpedienteJM
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteMedicoAlumModeloJM {

    suspend fun obtenerExpedientePorMatricula(token: String, matricula: String): Response<dataExpedienteJM> { // pide el expediente clinico del alumno por matricula
        return RetrofitCliente.apiServiceExpedienteJM.obtenerExpedientePorMatricula("Bearer $token", matricula)
    }

    fun armarIniciales(nombreCompleto: String?): String { // toma las dos primeras iniciales del nombre del alumno
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            return "--"
        }
        val partes = nombreCompleto.trim().split(" ").filter { it.isNotEmpty() }
        if (partes.size >= 2) {
            return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).uppercase()
        }
        return partes[0].take(2).uppercase()
    }

    fun formatearMatriculaEdad(matricula: String?, edad: Int): String { // junta la matricula y la edad en una sola linea
        val mat = matricula?.trim().orEmpty()
        if (mat.isNotEmpty() && edad > 0) {
            return "Matrícula $mat · $edad años"
        }
        if (mat.isNotEmpty()) {
            return "Matrícula $mat"
        }
        if (edad > 0) {
            return "$edad años"
        }
        return "Sin matrícula registrada"
    }

    fun formatearAlertaAlergia(alergias: String?): String { // arma el texto de alerta de alergias del paciente
        if (alergias != null && alergias.trim().isNotEmpty()) {
            return "Alergia: " + alergias.trim()
        }
        return "Sin alergias registradas"
    }

    fun resolverTexto(valor: String?, respaldo: String): String { // devuelve el valor si existe o un texto por defecto
        if (valor != null && valor.trim().isNotEmpty()) {
            return valor.trim()
        }
        return respaldo
    }

    fun obtenerConsultaPorIndice(
        consultas: List<Any>,
        indice: Int
    ): Any? { // devuelve la consulta reciente segun su posicion
        if (indice >= 0 && indice < consultas.size) {
            return consultas[indice]
        }
        return null
    }
}