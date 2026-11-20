
package com.example.servicios_medicos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCliente {

    private const val BASE_URL =
        "https://movil.uthhbola8.com/Servi_medicos/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiAutenticacion: ApiAutenticacionC =
        retrofit.create(ApiAutenticacionC::class.java)

    val apiAlumnoN: ApiAlumnoN =
        retrofit.create(ApiAlumnoN::class.java)
}
