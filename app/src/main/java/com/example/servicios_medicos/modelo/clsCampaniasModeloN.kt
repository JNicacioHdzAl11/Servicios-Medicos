package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente

class clsCampaniasModeloN {

    suspend fun listarCampanias(token: String): dataRespuestaN<List<dataCampaniaN>> {
        return RetrofitCliente.apiAlumno.listarCampanias("Bearer $token")
    }

    suspend fun obtenerDetalleCampania(token: String, idCampania: Int): dataRespuestaN<dataCampaniaN> {
        return RetrofitCliente.apiAlumno.obtenerDetalleCampania("Bearer $token", idCampania)
    }
}