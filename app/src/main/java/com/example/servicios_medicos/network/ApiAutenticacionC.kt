
package com.example.servicios_medicos.network

import com.example.servicios_medicos.modelo.dataCatalogosRespuestaC
import com.example.servicios_medicos.Data.dataCrearCuentaC
import com.example.servicios_medicos.Data.dataCrearCuentaRespuestaC
import com.example.servicios_medicos.Data.dataLoginC
import com.example.servicios_medicos.Data.dataLoginRespuestaC
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiAutenticacionC {

    @POST("login.php")
    suspend fun iniciarSesion(
        @Body datos: dataLoginC
    ): Response<dataLoginRespuestaC>

    @POST("crear_cuenta.php")
    suspend fun crearCuenta(
        @Body datos: dataCrearCuentaC
    ): Response<dataCrearCuentaRespuestaC>
    @GET("obtener_catalogos.php")
    suspend fun obtenerCatalogos(): Response<dataCatalogosRespuestaC>
}
