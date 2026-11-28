package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.dataConfigDoctoraRespuestaJM
import com.example.servicios_medicos.Data.dataOperacionDoctoraJM
import com.example.servicios_medicos.Data.dataPreferenciasDoctoraJM
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiConfigDoctoraJM {

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