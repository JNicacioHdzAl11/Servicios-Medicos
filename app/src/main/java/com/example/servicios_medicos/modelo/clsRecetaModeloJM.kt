package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsRecetaModeloJM {

    suspend fun obtenerReceta(token: String, idConsulta: Int): Response<dataRecetaJM> {
        return RetrofitCliente.apiServiceExpedienteJM.obtenerReceta("Bearer $token", idConsulta)
    }

    suspend fun guardarReceta(
        token: String,
        idConsulta: Int,
        medicamentos: List<MedicamentoRecetaJM>,
        indicaciones: String,
        emitir: Boolean
    ): Response<dataRespuestaP3JM> {
        val peticion = PeticionRecetaJM(
            idConsulta = idConsulta,
            indicaciones = indicaciones,
            medicamentos = medicamentos,
            estado = if (emitir) "Emitida" else "Borrador"
        )
        return RetrofitCliente.apiServiceExpedienteJM.generarReceta("Bearer $token", peticion)
    }

    fun filtrarMedicamentos(
        lista: List<MedicamentoRecetaJM>,
        consulta: String
    ): List<MedicamentoRecetaJM> {
        val filtro = consulta.trim().lowercase()
        if (filtro.isEmpty()) {
            return lista
        }
        return lista.filter { item ->
            item.nombre.lowercase().contains(filtro) ||
                    item.presentacionVia.lowercase().contains(filtro)
        }
    }

    fun formatearAlertaAlergia(alergia: String?): String {
        if (alergia.isNullOrBlank()) {
            return "Sin alergias registradas"
        }
        return "Alergia registrada: ${alergia.trim()}"
    }

    fun validarIndicaciones(indicaciones: String, medicamentos: List<MedicamentoRecetaJM>): String? {
        if (medicamentos.isEmpty()) {
            return "Agrega al menos un medicamento a la receta."
        }
        if (indicaciones.trim().isEmpty()) {
            return "Escribe las indicaciones médicas antes de continuar."
        }
        return null
    }
}