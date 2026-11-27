package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataDetalleDiagnosticoResponseJN
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsDetalleDiagnosticoModeloJN {

    suspend fun obtenerDetalleDiagnostico(  token: String,idConsulta: Int
    ): Response<dataDetalleDiagnosticoResponseJN> {
        return RetrofitCliente.apiExpediente.obtenerDetalleDiagnostico(
            token = "Bearer $token",
            idConsulta = idConsulta
        )
    }
}