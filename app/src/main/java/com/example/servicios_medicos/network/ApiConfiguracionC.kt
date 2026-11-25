package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataPerfilRespuestaC
import com.example.servicios_medicos.modelo.dataOperacionRespuestaC
import com.example.servicios_medicos.modelo.dataActualizarContrasenaC
import com.example.servicios_medicos.modelo.dataContactoEmergenciaC
import com.example.servicios_medicos.modelo.dataPreferenciasConfiguracionC
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiConfiguracionC {

    @GET("config/obtener_configuracion.php")
    fun obtenerConfiguracion(
        @Header("Authorization") token: String
    ): Call<dataPerfilRespuestaC>

    @POST("config/actualizar_contrasena.php")
    fun actualizarContrasena(
        @Header("Authorization") token: String,
        @Body datos: dataActualizarContrasenaC
    ): Call<dataOperacionRespuestaC>

    @POST("config/guardar_contacto.php")
    fun guardarContacto(
        @Header("Authorization") token: String,
        @Body datos: dataContactoEmergenciaC
    ): Call<dataOperacionRespuestaC>

    @POST("config/guardar_preferencias.php")
    fun guardarPreferencias(
        @Header("Authorization") token: String,
        @Body datos: dataPreferenciasConfiguracionC
    ): Call<dataOperacionRespuestaC>
}