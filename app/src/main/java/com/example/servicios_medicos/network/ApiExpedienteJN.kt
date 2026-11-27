package com.example.servicios_medicos.network

import com.example.servicios_medicos.Data.dataDetalleDiagnosticoResponseJN
import com.example.servicios_medicos.Data.dataExpedienteResponseJ
import com.example.servicios_medicos.Data.dataHistorialConsultasResponseJ
import com.example.servicios_medicos.Data.dataPerfilJN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServiceExpediente {

    @GET("expediente/obtener_perfil.php")
    suspend fun obtenerPerfil(
        @Header("Authorization") token: String
    ): Response<dataPerfilJN>

    @GET("expediente/obtener_expediente.php")
    suspend fun obtenerExpediente(
        @Header("Authorization") token: String
    ): Response<dataExpedienteResponseJ>

    @GET("expediente/historial_consultas.php")
    suspend fun obtenerHistorialConsultas(
        @Header("Authorization") token: String
    ): Response<dataHistorialConsultasResponseJ>

    @GET("expediente/obtener_detalle_diagnostico.php")
    suspend fun obtenerDetalleDiagnostico(
        @Header("Authorization") token: String,
        @Query("id_consulta") idConsulta: Int
    ): Response<dataDetalleDiagnosticoResponseJN>
}