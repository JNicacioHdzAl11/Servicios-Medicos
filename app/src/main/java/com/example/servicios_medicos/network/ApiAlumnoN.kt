package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.AgendarCitaRequestN
import com.example.servicios_medicos.modelo.AgendarCitaResponseN
import com.example.servicios_medicos.modelo.CancelarCitaRequestN
import com.example.servicios_medicos.modelo.CancelarCitaResponseN
import com.example.servicios_medicos.modelo.DetalleCitaResponseN
import com.example.servicios_medicos.modelo.HorariosDisponiblesResponseN
import com.example.servicios_medicos.modelo.ListaCitasResponseN
import com.example.servicios_medicos.Data.dataInicioAlumnoN
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiAlumnoN {

    @GET("alumno/inicio_alumno.php")
    suspend fun obtenerInicioAlumno(
        @Header("Authorization") tokenBearer: String
    ): dataInicioAlumnoN

    @GET("alumno/horarios_disponibles.php")
    suspend fun obtenerHorariosDisponibles(
        @Header("Authorization") tokenBearer: String,
        @Query("fecha") fecha: String
    ): HorariosDisponiblesResponseN

    @POST("alumno/agendar_cita.php")
    suspend fun agendarCita(
        @Header("Authorization") tokenBearer: String,
        @Body solicitud: AgendarCitaRequestN
    ): AgendarCitaResponseN

    @GET("alumno/listar_citas.php")
    suspend fun listarCitas(
        @Header("Authorization") tokenBearer: String
    ): ListaCitasResponseN

    @GET("alumno/detalle_cita.php")
    suspend fun obtenerDetalleCita(
        @Header("Authorization") tokenBearer: String,
        @Query("id_cita") idCita: Int
    ): DetalleCitaResponseN

    @POST("alumno/cancelar_cita.php")
    suspend fun cancelarCita(
        @Header("Authorization") tokenBearer: String,
        @Body solicitud: CancelarCitaRequestN
    ): CancelarCitaResponseN
}
