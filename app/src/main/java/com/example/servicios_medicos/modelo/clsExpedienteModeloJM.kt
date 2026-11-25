package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteModeloJM {

    suspend fun obtenerExpediente(token: String): Response<dataExpedienteAlumno> {
        return RetrofitCliente.apiExpedienteJM.obtenerExpedienteAlumno("Bearer $token")
    }

    suspend fun obtenerHistorial(token: String): Response<dataHistorialConsultas> {
        return RetrofitCliente.apiExpedienteJM.obtenerHistorialConsultas("Bearer $token")
    }

    suspend fun obtenerReceta(token: String, idConsulta: Int): Response<dataRecetaConsulta> {
        return RetrofitCliente.apiExpedienteJM.obtenerReceta("Bearer $token", idConsulta)
    }
}