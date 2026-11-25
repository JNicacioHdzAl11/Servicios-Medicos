package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.PeticionConsultaJM
import com.example.servicios_medicos.Data.PeticionRecetaJM
import com.example.servicios_medicos.Data.dataDetalleConsultaJM
import com.example.servicios_medicos.Data.dataExpedienteJM
import com.example.servicios_medicos.Data.dataPerfilJM
import com.example.servicios_medicos.Data.dataRecetaJM
import com.example.servicios_medicos.Data.dataRespuestaP3JM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceExpedienteJM {

    @GET("expediente/obtener_perfil.php")
    suspend fun obtenerPerfil(
        @Header("Authorization") tokenBearer: String
    ): Response<dataPerfilJM>

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerExpedienteAlumno(
        @Header("Authorization") tokenBearer: String
    ): Response<dataExpedienteJM>

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerExpedientePorMatricula(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Response<dataExpedienteJM>

    @GET("expediente/obtener_receta.php")
    suspend fun obtenerReceta(
        @Header("Authorization") tokenBearer: String,
        @Query("id_consulta") idConsulta: Int
    ): Response<dataRecetaJM>

    @POST("doctora/guardar_consulta.php")
    suspend fun guardarConsulta(
        @Header("Authorization") tokenBearer: String,
        @Body peticion: PeticionConsultaJM
    ): Response<dataRespuestaP3JM>

    @POST("doctora/generar_receta.php")
    suspend fun generarReceta(
        @Header("Authorization") tokenBearer: String,
        @Body peticion: PeticionRecetaJM
    ): Response<dataRespuestaP3JM>

    @POST("expediente/solicitar_expediente.php")
    suspend fun solicitarExpediente(
        @Header("Authorization") tokenBearer: String,
        @Query("matricula") matricula: String
    ): Response<dataRespuestaP3JM>

    @GET("doctora/obtener_consulta.php")
    suspend fun obtenerDetalleConsulta(
        @Header("Authorization") tokenBearer: String,
        @Query("id_consulta") idConsulta: Int
    ): Response<dataDetalleConsultaJM>
}