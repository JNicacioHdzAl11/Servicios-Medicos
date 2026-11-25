package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataCampaniaResponseJN
import com.example.servicios_medicos.modelo.dataDetalleCampaniaResponseJN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiCampania {

    @GET("alumno/listar_campanias.php")
    suspend fun obtenerCampanias(
        @Header("Authorization") token: String
    ): Response<dataCampaniaResponseJN>
    @GET("alumno/detalle_campania.php")
    suspend fun obtenerDetalleCampania(
        @Header("Authorization") token: String,
        @Query("id_campania") idCampania: Int
    ): Response<dataDetalleCampaniaResponseJN>
}
