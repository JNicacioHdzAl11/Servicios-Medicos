package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiAlumnoN {

    @GET("alumno/inicio_alumno.php")
    suspend fun obtenerInicioAlumno(
        @Header("Authorization") token: String
    ): dataRespuestaN<dataInicioAlumnoN>

    @GET("alumno/listar_citas.php")
    suspend fun listarCitas(
        @Header("Authorization") token: String
    ): dataRespuestaN<List<dataCitaN>>

    @GET("alumno/detalle_cita.php")
    suspend fun obtenerDetalleCita(
        @Header("Authorization") token: String,
        @Query("id_cita") idCita: Int
    ): dataRespuestaN<dataCitaN>

    @GET("alumno/horarios_disponibles.php")
    suspend fun obtenerHorariosDisponibles(
        @Header("Authorization") token: String,
        @Query("fecha") fecha: String
    ): dataRespuestaN<dataHorariosN>

    @GET("alumno/comprobante_ingreso.php")
    suspend fun obtenerComprobanteIngreso(
        @Header("Authorization") token: String,
        @Query("id_cita") idCita: Int
    ): dataRespuestaN<dataComprobanteQrN>

    @GET("alumno/listar_campanias.php")
    suspend fun listarCampanias(
        @Header("Authorization") token: String
    ): dataRespuestaN<List<dataCampaniaN>>

    @GET("alumno/detalle_campania.php")
    suspend fun obtenerDetalleCampania(
        @Header("Authorization") token: String,
        @Query("id_campania") idCampania: Int
    ): dataRespuestaN<dataCampaniaN>

    @POST("alumno/agendar_cita.php")
    suspend fun agendarCita(
        @Header("Authorization") token: String,
        @Body solicitud: dataAgendarCitaRequestN
    ): dataRespuestaN<dataCitaAgendadaN>

    @POST("alumno/cancelar_cita.php")
    suspend fun cancelarCita(
        @Header("Authorization") token: String,
        @Body solicitud: dataCancelarCitaRequestN
    ): dataRespuestaN<dataCitaCanceladaN>
}