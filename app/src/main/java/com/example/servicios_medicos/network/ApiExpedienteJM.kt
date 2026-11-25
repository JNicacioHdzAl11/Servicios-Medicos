package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataExpedienteAlumno
import com.example.servicios_medicos.modelo.dataHistorialConsultas
import com.example.servicios_medicos.modelo.dataRecetaConsulta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiExpedienteJM {

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerExpedienteAlumno(
        @Header("Authorization") tokenBearer: String
    ): Response<dataExpedienteAlumno>

    @GET("expediente/historial_consultas.php")
    suspend fun obtenerHistorialConsultas(
        @Header("Authorization") tokenBearer: String
    ): Response<dataHistorialConsultas>

    @GET("expediente/obtener_receta.php")
    suspend fun obtenerReceta(
        @Header("Authorization") tokenBearer: String,
        @Query("id_consulta") idConsulta: Int
    ): Response<dataRecetaConsulta>
}