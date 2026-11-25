package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.ConsultaItemJM
import com.example.servicios_medicos.Data.dataExpedienteJM
import com.example.servicios_medicos.Data.dataRecetaJM
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteAlumModeloJM {

    suspend fun obtenerExpedientePorMatricula(token: String, matricula: String): Response<dataExpedienteJM> { // llama a la api expediente por matricula
        return RetrofitCliente.apiExpedienteJM.obtenerExpedientePorMatricula("Bearer $token", matricula)
    }

    suspend fun obtenerRecetaAnterior(token: String, idConsulta: Int): Response<dataRecetaJM> { // llama a la api obtener receta
        return RetrofitCliente.apiExpedienteJM.obtenerReceta("Bearer $token", idConsulta)
    }

    fun armarIniciales(nombreCompleto: String): String { // arma las iniciales del alumno
        val partes = nombreCompleto.trim().split(" ").filter { it.isNotEmpty() }
        if (partes.isEmpty()) {
            return "AL"
        }
        if (partes.size == 1) {
            return partes[0].take(2).uppercase()
        }
        return (partes[0].take(1) + partes[1].take(1)).uppercase()
    }

    fun formatearMatriculaEdad(matricula: String, edad: String): String { // arma el texto de matricula y edad
        if (edad.isNotEmpty()) {
            return "Matrícula $matricula · $edad"
        }
        return "Matrícula $matricula"
    }

    fun formatearAlertaAlergia(alergias: String): String { // arma el texto de alerta de alergia
        val valor = resolverTexto(alergias, "Ninguna")
        return "Alergia: $valor"
    }

    fun resolverTexto(valor: String, respaldo: String): String { // regresa el valor o el texto de respaldo si esta vacio
        if (valor.isNotEmpty()) {
            return valor
        }
        return respaldo
    }

    fun obtenerConsultaPorIndice(consultas: List<ConsultaItemJM>, indice: Int): ConsultaItemJM? { // regresa la consulta en el indice indicado
        if (indice >= 0 && consultas.size > indice) {
            return consultas[indice]
        }
        return null
    }

    fun formatearResumenReceta(medicamentos: String, indicaciones: String): String { // arma el texto de resumen de receta
        return "$medicamentos · $indicaciones"
    }
}