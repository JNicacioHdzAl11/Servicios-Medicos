package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.Data.dataExpedienteDoctoraJM
import com.example.servicios_medicos.network.ApiDoctoraJM
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clsExpedienteAlumnoModeloJM {
    private val api = Retrofit.Builder()
        .baseUrl("https://movil.uthhbola8.com/Servi_medicos/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiDoctoraJM::class.java)

    fun obtenerExpedienteAlumno(token: String, matricula: String): Call<dataExpedienteDoctoraJM> { // llama a la api expediente del alumno
        return api.obtenerExpedienteAlumno("Bearer $token", matricula)
    }

    fun armarIniciales(nombre: String): String { // arma las iniciales del alumno
        val partes = nombre.trim().split(" ").filter { it.isNotEmpty() }
        if (partes.isEmpty()) {
            return "AL"
        }
        if (partes.size == 1) {
            return partes[0].take(2).uppercase()
        }
        return (partes[0].take(1) + partes[1].take(1)).uppercase()
    }

    fun formatearMatriculaEdad(matricula: String, edad: String): String { // arma matricula y edad
        if (edad.isNotEmpty()) {
            return "Matrícula $matricula · $edad"
        }
        return "Matrícula $matricula"
    }
}