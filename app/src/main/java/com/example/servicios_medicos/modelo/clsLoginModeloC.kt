
package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsLoginModeloC {

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Response<dataLoginRespuestaC> {

        val datos = dataLoginC(correo, contrasena)

        return RetrofitCliente.apiAutenticacion
            .iniciarSesion(datos)
    }
}
