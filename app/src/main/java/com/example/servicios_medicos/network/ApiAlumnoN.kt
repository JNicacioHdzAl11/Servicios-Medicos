package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataInicioAlumnoN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiAlumnoN {
    @GET("alumno/inicio_alumno.php")
    suspend fun obtenerInicioAlumno(
        @Header("Authorization") tokenBearer: String
    ): Response<dataInicioAlumnoN>
}