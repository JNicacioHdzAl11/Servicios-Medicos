package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente

class clsInicioAlumnoModeloN {

    suspend fun obtenerInicioAlumno(token: String): dataRespuestaN<dataInicioAlumnoN> {
        return RetrofitCliente.apiAlumno.obtenerInicioAlumno("Bearer $token")
    }
}