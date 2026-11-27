package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataCampaniaResponseJN
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsCampaniasModeloN {

    suspend fun obtenerCampanias(token: String): Response<dataCampaniaResponseJN> {
        return RetrofitCliente.apiCampanias.obtenerCampanias("Bearer $token")
    }
}

