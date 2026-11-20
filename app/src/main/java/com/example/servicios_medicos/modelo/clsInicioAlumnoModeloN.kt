package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsInicioAlumnoModeloN {
    suspend fun obtenerDatosInicio(token: String): Response<dataInicioAlumnoN> {
        return RetrofitCliente.apiAlumnoN.obtenerInicioAlumno("Bearer $token")
    }
}