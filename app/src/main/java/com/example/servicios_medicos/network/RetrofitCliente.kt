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

<<<<<<< Updated upstream
    }
}
=======
    val apiAutenticacion: ApiAutenticacionC =
        retrofit.create(ApiAutenticacionC::class.java)

    val apiAlumnoN: ApiAlumnoN =
        retrofit.create(ApiAlumnoN::class.java)

    val apiRecetaJM: ApiRecetaJM=
        retrofit.create(ApiRecetaJM::class.java)

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
>>>>>>> Stashed changes
