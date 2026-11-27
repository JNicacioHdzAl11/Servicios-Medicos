package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataExpedienteResponseJ
import com.example.servicios_medicos.Data.dataHistorialConsultasResponseJ
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteAlumnoModeloJM {

    suspend fun obtenerExpediente(token: String): Response<dataExpedienteResponseJ> {
        return RetrofitCliente.apiExpediente.obtenerExpediente("Bearer $token")
    }

    suspend fun obtenerHistorialConsultas(token: String): Response<dataHistorialConsultasResponseJ> {
        return RetrofitCliente.apiExpediente.obtenerHistorialConsultas("Bearer $token")
    }
}