package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente

class clsLoginModeloC {

    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): dataLoginRespuestaC {

        val solicitud = dataLoginC(
            correo = correo,
            contrasena = contrasena
        )

        return RetrofitCliente.apiService.iniciarSesion(solicitud)
    }
}