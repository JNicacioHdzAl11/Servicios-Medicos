package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsDetalleCampaniaModeloN {

    suspend fun obtenerDetalleCampania(token: String, idCampania: Int): Response<dataDetalleCampaniaResponseJN> {
        return RetrofitCliente.apiCampanias.obtenerDetalleCampania("Bearer $token", idCampania)
    }
}