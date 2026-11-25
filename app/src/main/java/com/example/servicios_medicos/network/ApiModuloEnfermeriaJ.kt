package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataAlumnoEnfermeriaJ
import com.example.servicios_medicos.modelo.dataCitaEnfermeriaJ
import com.example.servicios_medicos.modelo.dataRespuestaEnfermeriaJ
import com.example.servicios_medicos.modelo.dataSignosEnfermeriaJ
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiModuloEnfermeriaJ {

    @GET("enfermeria/listar_alumnos.php")
    fun listarAlumnos(@Header("Authorization") token: String): Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>>

    @GET("enfermeria/citas_pendientes.php")
    fun citasPendientes(@Header("Authorization") token: String): Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>

    @POST("enfermeria/registrar_signos.php")
    fun registrarSignos(@Header("Authorization") token: String, @Body datos: dataSignosEnfermeriaJ): Call<dataRespuestaEnfermeriaJ<JsonElement>>
}
