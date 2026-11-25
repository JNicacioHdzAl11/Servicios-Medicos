package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataExpedienteJM
import com.example.servicios_medicos.Data.dataRespuestaP3JM
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteAlumnoModeloJM {

    suspend fun obtenerExpedienteAlumno(token: String): Response<dataExpedienteJM> { // pide el expediente del alumno autenticado
        return RetrofitCliente.apiServiceExpedienteJM.obtenerExpedienteAlumno("Bearer $token")
    }

    suspend fun solicitarExpedienteCompleto(token: String, matricula: String): Response<dataRespuestaP3JM> { // envia la solicitud de expediente completo
        return RetrofitCliente.apiServiceExpedienteJM.solicitarExpediente("Bearer $token", matricula)
    }

    fun formatearMatriculaActualizacion(matricula: String?, fechaActualizacion: String?): String { // arma la linea de matricula y ultima actualizacion
        val mat = matricula?.trim().orEmpty()
        val fecha = fechaActualizacion?.trim().orEmpty()
        if (mat.isNotEmpty() && fecha.isNotEmpty()) {
            return "Matrícula $mat · Actualizado el $fecha"
        }
        if (mat.isNotEmpty()) {
            return "Matrícula $mat"
        }
        if (fecha.isNotEmpty()) {
            return "Actualizado el $fecha"
        }
        return "Expediente actualizado"
    }

    fun resolverTexto(valor: String?, respaldo: String): String { // regresa el dato si existe o un texto de respaldo
        if (valor != null && valor.trim().isNotEmpty()) {
            return valor.trim()
        }
        return respaldo
    }

    fun obtenerConsultasRecientesLimitadas(
        consultas: List<Any>,
        limite: Int = 2
    ): List<Any> { // devuelve las consultas recientes mas recientes hasta el limite indicado
        if (consultas.size <= limite) {
            return consultas
        }
        return consultas.take(limite)
    }
}