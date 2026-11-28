package com.example.servicios_medicos.Data

data class AlumnoExpedienteDoctoraJM(
    val matricula: String? = null,
    val nombreCompleto: String? = null,
    val area: String? = null,
    val cuatrimestre: String? = null,
    val grupo: String? = null
)

data class DetalleExpedienteDoctoraJM(
    val edad: String? = null,
    val alergias: String? = null,
    val antecedentes: String? = null,
    val tipoSangre: String? = null,
    val enfermedades: String? = null
)

data class HistorialConsultaDoctoraJM(
    val fecha: String? = null,
    val fechaConsulta: String? = null,
    val motivo: String? = null,
    val diagnostico: String? = null
)

data class ExpedienteAlumnoDoctoraDetalleJM(
    val alumno: AlumnoExpedienteDoctoraJM? = null,
    val expediente: DetalleExpedienteDoctoraJM? = null,
    val historialConsultas: List<HistorialConsultaDoctoraJM> = emptyList()
)

data class dataExpedienteDoctoraJM(
    val status: Boolean? = null,
    val data: ExpedienteAlumnoDoctoraDetalleJM? = null,
    val message: String? = null
)