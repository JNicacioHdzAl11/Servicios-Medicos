package com.example.servicios_medicos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitCliente {
    const val BASE_URL = "http://10.0.2.2/Servi_medicos/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiAutenticacionC by lazy {
        retrofit.create(ApiAutenticacionC::class.java)
    }

    val apiAutenticacion: ApiAutenticacionC by lazy {
        retrofit.create(ApiAutenticacionC::class.java)
    }

    val apiAlumnoN: ApiAlumnoN by lazy {
        retrofit.create(ApiAlumnoN::class.java)
    }

    val apiRecetaJM: ApiRecetaJM by lazy {
        retrofit.create(ApiRecetaJM::class.java)
    }

    val apiConsultaJM: ApiConsultaJM by lazy {
        retrofit.create(ApiConsultaJM::class.java)
    }

    val apiExpedienteAlumJM: ApiExpedienteAlumJM by lazy {
        retrofit.create(ApiExpedienteAlumJM::class.java)
    }

    val apiExpedienteJM: ApiExpedienteJM by lazy {
        retrofit.create(ApiExpedienteJM::class.java)
    }
}