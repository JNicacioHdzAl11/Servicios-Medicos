package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response

class clsConsultaMedicaModeloJM {

    suspend fun obtenerDetalleConsulta(token: String, idConsulta: Int): Response<dataDetalleConsultaJM> { // pide los datos clinicos de la consulta
        return RetrofitCliente.apiServiceExpedienteJM.obtenerDetalleConsulta("Bearer $token", idConsulta)
    }

    suspend fun guardarConsulta( // envia la consulta medica al backend para guardarla
        token: String,
        idConsulta: Int,
        matricula: String,
        motivo: String,
        padecimiento: String,
        sintomas: String,
        diagnostico: String
    ): Response<dataRespuestaP3JM> {
        val peticion = PeticionConsultaJM(
            idCita = idConsulta,
            matricula = matricula,
            motivoConsulta = motivo,
            padecimientoActual = padecimiento,
            sintomasManifestados = sintomas,
            diagnosticoMedico = diagnostico
        )
        return RetrofitCliente.apiServiceExpedienteJM.guardarConsulta("Bearer $token", peticion)
    }

    fun validarCamposConsulta( // revisa que los campos clinicos obligatorios esten llenos
        motivo: String,
        padecimiento: String,
        sintomas: String,
        diagnostico: String
    ): String? {
        if (motivo.trim().isEmpty()) {
            return "Escribe el motivo de la consulta."
        }
        if (padecimiento.trim().isEmpty()) {
            return "Escribe el padecimiento actual."
        }
        if (sintomas.trim().isEmpty()) {
            return "Escribe los síntomas manifestados."
        }
        if (diagnostico.trim().isEmpty()) {
            return "Escribe el diagnóstico médico."
        }
        return null
    }

    fun formatearAlergia(alergia: String?): String { // arma el texto de alerta de alergia del alumno
        if (alergia != null && alergia.trim().isNotEmpty()) {
            return "Alergia: " + alergia.trim()
        }
        return "Sin alergias registradas"
    }

    fun formatearSigno(etiqueta: String, valor: String?, unidad: String): String { // da formato a cada signo vital
        if (valor != null && valor.trim().isNotEmpty()) {
            return "$etiqueta: ${valor.trim()} $unidad"
        }
        return "$etiqueta: --"
    }
}