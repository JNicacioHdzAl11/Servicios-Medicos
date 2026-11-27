package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.dataPerfilJM
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiPerfilJM {

    @GET("expediente/obtener_perfil.php")
    suspend fun obtenerPerfil(
        @Header("Authorization") tokenBearer: String
    ): Response<dataPerfilJM>
}