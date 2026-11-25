package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataInicioAlumnoN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: InicioAlumnoDataN? = null
)

data class InicioAlumnoDataN(
    @SerializedName("alumno")
    val alumno: AlumnoInicioModeloN? = null,

    @SerializedName("proxima_cita")
    val proximaCita: CitaModeloN? = null,

    @SerializedName("campanias_activas")
    val campaniasActivas: List<CampaniaModeloN> = emptyList()
)

data class AlumnoInicioModeloN(
    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",
)

data class CitaModeloN(
    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("hora")
    val hora: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("estado")
    val estado: String = ""
)

data class CampaniaModeloN(
    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("fecha_inicio")
    val fechaInicio: String = "",

    @SerializedName("fecha_fin")
    val fechaFin: String = "",

    @SerializedName("lugar")
    val lugar: String = "",
)