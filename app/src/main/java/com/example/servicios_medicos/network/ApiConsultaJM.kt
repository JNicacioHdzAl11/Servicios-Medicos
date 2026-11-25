package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.PeticionGuardarConsulta
import com.example.servicios_medicos.modelo.dataConsultaMedicaJM
import com.example.servicios_medicos.modelo.dataRespuestaConsulta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiConsultaJM {

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerDatosConsulta(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Response<dataConsultaMedicaJM>

    @POST("expediente/actualizar_datos.php")
    suspend fun guardarConsultaMedica(
        @Header("Authorization") tokenBearer: String,
        @Body peticion: PeticionGuardarConsulta
    ): Response<dataRespuestaConsulta>
}