package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataActualizarContrasenaC
import com.example.servicios_medicos.Data.dataContactoEmergenciaC
import com.example.servicios_medicos.Data.dataOperacionRespuestaC
import com.example.servicios_medicos.Data.dataPerfilRespuestaC
import com.example.servicios_medicos.Data.dataPreferenciasConfiguracionC
import com.example.servicios_medicos.network.ApiConfiguracionC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call

class clsConfiguracionModeloC {
    private val api = Retrofit.Builder()
        .baseUrl("https://movil.uthhbola8.com/Servi_medicos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiConfiguracionC::class.java)

    fun obtenerConfiguracion(token: String): Call<dataPerfilRespuestaC> {
        return api.obtenerConfiguracion("Bearer $token")
    }

    fun actualizarContrasena(token: String, actual: String, nueva: String, confirmar: String): Call<dataOperacionRespuestaC> {
        val datos = dataActualizarContrasenaC(actual, nueva, confirmar)
        return api.actualizarContrasena("Bearer $token", datos)
    }

    fun guardarContacto(token: String, nombre: String, parentesco: String, telefono: String): Call<dataOperacionRespuestaC> {
        val datos = dataContactoEmergenciaC(nombre, parentesco, telefono)
        return api.guardarContacto("Bearer $token", datos)
    }

    fun guardarPreferencias(token: String, recordatorios: Boolean, campanias: Boolean): Call<dataOperacionRespuestaC> {
        val datos = dataPreferenciasConfiguracionC(recordatorios, campanias)
        return api.guardarPreferencias("Bearer $token", datos)
    }
}
