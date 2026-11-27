package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataDetalleDiagnosticoResponseJN(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: dataDetalleDiagnosticoJN?
)

data class dataDetalleDiagnosticoJN(
    @SerializedName("id_consulta") val idConsulta: Int,
    @SerializedName("fecha_consulta") val fechaConsulta: String,
    @SerializedName("nombre_paciente") val nombrePaciente: String,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("folio_receta") val folioReceta: String,
    @SerializedName("nombre_medico") val nombreMedico: String,
    @SerializedName("cedula_profesional") val cedulaProfesional: String,
    @SerializedName("consultorio") val consultorio: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("nombre_diagnostico") val nombreDiagnostico: String,
    @SerializedName("descripcion_diagnostico") val descripcionDiagnostico: String,
    @SerializedName("medicamentos") val medicamentos: List<dataMedicamentoJN>
)

data class dataMedicamentoJN(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("concentracion") val concentracion: String,
    @SerializedName("dosis") val dosis: String,
    @SerializedName("via") val via: String,
    @SerializedName("frecuencia") val frecuencia: String,
    @SerializedName("horarios") val horarios: String,
    @SerializedName("duracion") val duracion: String
)
