package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.PeticionConsultaJM
import com.example.servicios_medicos.modelo.dataDetalleConsultaJM
import com.example.servicios_medicos.modelo.dataRespuestaP3JM
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
    ): Response<dataDetalleConsultaJM>

    @POST("expediente/actualizar_datos.php")
    suspend fun guardarConsultaMedica(
        @Header("Authorization") tokenBearer: String,
        @Body peticion: PeticionConsultaJM
    ): Response<dataRespuestaP3JM>
}