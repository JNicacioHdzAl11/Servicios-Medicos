package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataCrearCuentaC
import com.example.servicios_medicos.Data.dataCrearCuentaRespuestaC
import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsCrearCuentaModeloC {

    suspend fun obtenerCatalogos(): Response<dataCatalogosRespuestaC> {

        return RetrofitCliente.apiAutenticacion
            .obtenerCatalogos()
    }

    suspend fun crearCuenta(
        nombre: String,
        matricula: String,
        correo: String,
        contrasena: String,
        confirmar: String,
        idArea: Int,
        idGrupo: Int,
        idCuatrimestre: Int
    ): Response<dataCrearCuentaRespuestaC> {

        val datos = dataCrearCuentaC(
            nombre,
            matricula,
            correo,
            contrasena,
            confirmar,
            idArea,
            idGrupo,
            idCuatrimestre
        )

        return RetrofitCliente.apiAutenticacion
            .crearCuenta(datos)
    }
}