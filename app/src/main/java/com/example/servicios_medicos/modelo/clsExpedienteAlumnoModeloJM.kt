package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsExpedienteAlumnoModeloJM {

    suspend fun obtenerExpediente(token: String, matricula: String): Response<dataExpedienteAlumJM> {
        return RetrofitCliente.apiExpedienteAlumJM.obtenerExpedienteAlumnoMedico("Bearer $token", matricula)
    }

    suspend fun obtenerRecetaAnterior(token: String, idConsulta: Int): Response<dataRecetaAnteriorMedico> {
        return RetrofitCliente.apiExpedienteAlumJM.obtenerRecetaAnterior("Bearer $token", idConsulta)
    }
}