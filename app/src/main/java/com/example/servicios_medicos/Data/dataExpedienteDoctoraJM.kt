package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataExpedienteDoctoraJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: ExpedienteDoctoraDataJM? = null
)

data class ExpedienteDoctoraDataJM(
    @SerializedName("iniciales") val iniciales: String = "",
    @SerializedName("nombre_paciente") val nombrePaciente: String = "",
    @SerializedName("matricula") val matricula: String = "",
    @SerializedName("edad") val edad: String = "",
    @SerializedName("carrera_grupo") val carreraGrupo: String = "",
    @SerializedName("alergia") val alergia: String = "",
    @SerializedName("tipo_sangre") val tipoSangre: String = "",
    @SerializedName("enfermedades_cronicas") val enfermedadesCronicas: String = "",
    @SerializedName("alergenos_detalle") val alergenosDetalle: String = "",
    @SerializedName("cirugias_previas") val cirugiasPrevias: String = "",
    @SerializedName("tratamiento_habitual") val tratamientoHabitual: String = "",
    @SerializedName("consulta_1") val consulta1: String = "",
    @SerializedName("consulta_2") val consulta2: String = ""
)