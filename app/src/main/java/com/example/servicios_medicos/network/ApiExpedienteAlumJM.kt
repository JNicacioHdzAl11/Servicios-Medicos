package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataExpedienteAlumno
import com.example.servicios_medicos.modelo.dataRecetaConsulta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiExpedienteAlumJM {

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerExpedienteAlumnoMedico(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Response<dataExpedienteAlumno>

    @GET("expediente/obtener_receta.php")
    suspend fun obtenerRecetaAnterior(
        @Header("Authorization") tokenBearer: String,
        @Query("id_consulta") idConsulta: Int
    ): Response<dataRecetaConsulta>
}