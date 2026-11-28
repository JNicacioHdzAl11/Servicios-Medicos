package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataConfigDoctoraRespuestaJM
import com.example.servicios_medicos.Data.dataOperacionDoctoraJM
import com.example.servicios_medicos.Data.dataPreferenciasDoctoraJM
import com.example.servicios_medicos.network.ApiConfigDoctoraJM
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clsConfigDoctoraModeloJM {
    private val api = Retrofit.Builder()
        .baseUrl("https://movil.uthhbola8.com/Servi_medicos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiConfigDoctoraJM::class.java)

    fun obtenerConfiguracion(token: String): Call<dataConfigDoctoraRespuestaJM> {
        return api.obtenerConfiguracion("Bearer $token")
    }

    fun guardarPreferencias(
        token: String,
        recordatoriosCitas: Boolean,
        avisosCampanias: Boolean
    ): Call<dataOperacionDoctoraJM> {
        val datos = dataPreferenciasDoctoraJM(
            recordatoriosCitas = recordatoriosCitas,
            avisosCampanias = avisosCampanias
        )
        return api.guardarPreferencias("Bearer $token", datos)
    }
}