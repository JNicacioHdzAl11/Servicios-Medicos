package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.ApiModuloEnfermeriaJ
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clsModuloEnfermeriaModeloJ {

    private val api = Retrofit.Builder()
        .baseUrl("https://movil.uthhbola8.com/Servi_medicos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiModuloEnfermeriaJ::class.java)

    fun listarAlumnos(token: String): Call<dataRespuestaEnfermeriaJ<List<dataAlumnoEnfermeriaJ>>> {
        return api.listarAlumnos("Bearer $token")
    }

    fun citasPendientes(token: String): Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>> {
        return api.citasPendientes("Bearer $token")
    }

    fun registrarSignos(token: String, datos: dataSignosEnfermeriaJ): Call<dataRespuestaEnfermeriaJ<JsonElement>> {
        return api.registrarSignos("Bearer $token", datos)
    }
}
