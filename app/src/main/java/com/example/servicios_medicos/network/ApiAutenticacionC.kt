package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataLoginC
import com.example.servicios_medicos.modelo.dataLoginRespuestaC
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAutenticacionC {
    @POST("login.php")
    suspend fun iniciarSesion(
        @Body solicitud: dataLoginC
    ): dataLoginRespuestaC
}