package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.AgendarCitaRequestN
import com.example.servicios_medicos.modelo.AgendarCitaResponseN
import com.example.servicios_medicos.modelo.HorariosDisponiblesResponseN
import com.example.servicios_medicos.modelo.dataInicioAlumnoN
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import com.example.servicios_medicos.modelo.CancelarCitaRequestN
import com.example.servicios_medicos.modelo.CancelarCitaResponseN
import com.example.servicios_medicos.modelo.DetalleCitaResponseN
import com.example.servicios_medicos.modelo.ListaCitasResponseN

interface ApiAlumnoN {

    @GET("alumno/inicio_alumno.php")
    suspend fun obtenerInicioAlumno(
        @Header("Authorization") tokenBearer: String
    ): Response<dataInicioAlumnoN>

    @GET("alumno/horarios_disponibles.php")
    suspend fun obtenerHorariosDisponibles(
        @Header("Authorization") tokenBearer: String,
        @Query("fecha") fecha: String
    ): Response<HorariosDisponiblesResponseN>

    @POST("alumno/agendar_cita.php")
    suspend fun agendarCita(
        @Header("Authorization") tokenBearer: String,
        @Body solicitud: AgendarCitaRequestN
    ): Response<AgendarCitaResponseN>
    @GET("alumno/listar_citas.php")
    suspend fun listarCitas(
        @Header("Authorization") tokenBearer: String
    ): Response<ListaCitasResponseN>

    @GET("alumno/detalle_cita.php")
    suspend fun obtenerDetalleCita(
        @Header("Authorization") tokenBearer: String,
        @Query("id_cita") idCita: Int
    ): Response<DetalleCitaResponseN>

    @POST("alumno/cancelar_cita.php")
    suspend fun cancelarCita(
        @Header("Authorization") tokenBearer: String,
        @Body solicitud: CancelarCitaRequestN
    ): Response<CancelarCitaResponseN>
}