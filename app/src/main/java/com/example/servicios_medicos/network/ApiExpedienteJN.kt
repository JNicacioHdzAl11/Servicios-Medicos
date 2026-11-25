package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataExpedienteResponseJ
import com.example.servicios_medicos.modelo.dataHistorialConsultasResponseJ
import com.example.servicios_medicos.modelo.dataPerfilJN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

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
}