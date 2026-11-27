package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.network.ApiDoctoraJM
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clsInicioDoctoraModeloJM {
    private val api = Retrofit.Builder()
        .baseUrl("https://movil.uthhbola8.com/Servi_medicos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiDoctoraJM::class.java)

    fun obtenerInicioDoctora(token: String): Call<dataInicioDoctoraJM> {
        return api.obtenerInicioDoctora("Bearer $token")
    }
}