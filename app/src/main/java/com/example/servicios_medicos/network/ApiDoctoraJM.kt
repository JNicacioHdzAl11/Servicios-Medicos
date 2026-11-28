package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.dataConfigDoctoraRespuestaJM
import com.example.servicios_medicos.Data.dataExpedienteDoctoraJM
import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.Data.dataOperacionDoctoraJM
import com.example.servicios_medicos.Data.dataPreferenciasDoctoraJM
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiDoctoraJM {

    @GET("doctora/inicio_doctora.php")
    suspend fun obtenerInicioDoctora(
        @Header("Authorization") tokenBearer: String
    ): Response<dataInicioDoctoraJM>

    @GET("doctora/expediente_alumno.php")
    fun obtenerExpedienteAlumno(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Call<dataExpedienteDoctoraJM>

    @GET("config/obtener_configuracion.php")
    fun obtenerConfiguracion(
        @Header("Authorization") tokenBearer: String
    ): Call<dataConfigDoctoraRespuestaJM>

    @POST("config/guardar_preferencias.php")
    fun guardarPreferencias(
        @Header("Authorization") tokenBearer: String,
        @Body datos: dataPreferenciasDoctoraJM
    ): Call<dataOperacionDoctoraJM>
}