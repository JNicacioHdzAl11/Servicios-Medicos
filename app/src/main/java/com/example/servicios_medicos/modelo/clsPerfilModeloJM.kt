package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataPerfilJN
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsPerfilModeloJM {

    suspend fun obtenerPerfil(token: String): Response<dataPerfilJN> {
        return RetrofitCliente.apiExpediente.obtenerPerfil("Bearer $token")
    }
}