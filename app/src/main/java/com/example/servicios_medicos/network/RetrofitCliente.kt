package com.example.servicios_medicos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitCliente {
    const val BASE_URL = "http://10.0.2.2/Servi_medicos/"

    val apiService: ApiAutenticacionC by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiAutenticacionC::class.java)

    }
    val apiAlumno: ApiAlumnoN by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiAlumnoN::class.java)
    }
}