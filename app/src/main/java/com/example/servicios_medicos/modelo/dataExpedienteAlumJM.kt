package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataExpedienteAlumJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: ExpedienteMedicoData? = null
)

data class ExpedienteMedicoData(
    @SerializedName("iniciales")
    val iniciales: String = "",

    @SerializedName("nombre_paciente")
    val nombrePaciente: String = "",

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("edad")
    val edad: String = "",

    @SerializedName("carrera_grupo")
    val carreraGrupo: String = "",

    @SerializedName("alergia")
    val alergia: String = "",

    @SerializedName("tipo_sangre")
    val tipoSangre: String = "",

    @SerializedName("enfermedades_cronicas")
    val enfermedadesCronicas: String = "",

    @SerializedName("alergenos_detalle")
    val alergenosDetalle: String = "",

    @SerializedName("cirugias_previas")
    val cirugiasPrevias: String = "",

    @SerializedName("tratamiento_habitual")
    val tratamientoHabitual: String = "",

    @SerializedName("consultas_recientes")
    val consultasRecientes: List<ConsultaMedicoModelo> = emptyList()
)

data class ConsultaMedicoModelo(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("fecha_hora")
    val fechaHora: String = "",

    @SerializedName("estado")
    val estado: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("diagnostico")
    val diagnostico: String = ""
)

data class dataRecetaAnteriorMedico(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val receta: RecetaAnteriorDetalle? = null
)

data class RecetaAnteriorDetalle(
    @SerializedName("id_receta")
    val idReceta: Int = 0,

    @SerializedName("medicamentos")
    val medicamentos: String = "",

    @SerializedName("indicaciones")
    val indicaciones: String = ""
)