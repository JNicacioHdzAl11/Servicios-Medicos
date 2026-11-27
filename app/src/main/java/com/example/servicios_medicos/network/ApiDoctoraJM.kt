package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.dataConfigDoctoraRespuestaJM
import com.example.servicios_medicos.Data.dataExpedienteDoctoraJM
import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.Data.dataOperacionDoctoraJM
import com.example.servicios_medicos.Data.dataPreferenciasDoctoraJM
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiDoctoraJM {

    @GET("doctora/inicio_doctora.php")
    fun obtenerInicioDoctora(
        @Header("Authorization") tokenBearer: String
    ): Call<dataInicioDoctoraJM>

    @GET("expediente/obtener_expediente.php")
    fun obtenerExpedienteAlumno(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Call<dataExpedienteDoctoraJM>

    @GET("doctora/configuracion_doctora.php")
    fun obtenerConfiguracion(
        @Header("Authorization") tokenBearer: String
    ): Call<dataConfigDoctoraRespuestaJM>

    @POST("doctora/guardar_configuracion.php")
    fun guardarPreferencias(
        @Header("Authorization") tokenBearer: String,
        @Body datos: dataPreferenciasDoctoraJM
    ): Call<dataOperacionDoctoraJM>
}