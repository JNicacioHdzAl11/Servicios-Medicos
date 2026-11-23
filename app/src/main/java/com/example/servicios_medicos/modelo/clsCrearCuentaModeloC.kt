
package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsCrearCuentaModeloC {

    suspend fun crearCuenta(
        nombre: String,
        matricula: String,
        correo: String,
        contrasena: String,
        confirmar: String
    ): Response<dataCrearCuentaRespuestaC> {

        val datos = dataCrearCuentaC(
            nombre,
            matricula,
            correo,
            contrasena,
            confirmar
        )

        return RetrofitCliente.apiAutenticacion
            .crearCuenta(datos)
    }
}
