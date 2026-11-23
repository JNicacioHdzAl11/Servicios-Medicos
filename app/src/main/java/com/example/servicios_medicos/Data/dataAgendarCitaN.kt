package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class HorariosDisponiblesResponseN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: HorariosDataN? = null
)
data class HorariosDataN(
    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("horarios")
    val horarios: List<String> = emptyList()
)
data class AgendarCitaRequestN(
    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("hora")
    val hora: String,

    @SerializedName("motivo")
    val motivo: String
)
data class AgendarCitaResponseN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: CitaAgendadaDataN? = null
)
data class CitaAgendadaDataN(
    @SerializedName("id_cita")
    val idCita: Int = 0,

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("hora")
    val hora: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("estado")
    val estado: String = "",

    @SerializedName("codigo_qr")
    val codigoQr: String = ""
)
data class DiaSemanaN(
    val letra: String = "",
    val numero: String = "",
    val fechaServidor: String = "",
    val habilitado: Boolean = true
)
data class ListaCitasResponseN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<CitaDetalleN>? = null
)
data class DetalleCitaResponseN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: CitaDetalleN? = null
)
data class CitaDetalleN(
    @SerializedName("id_cita")
    val idCita: Int = 0,

    @SerializedName("matricula")
    val matricula: String? = "",

    @SerializedName("fecha")
    val fecha: String? = "",

    @SerializedName("hora")
    val hora: String? = "",

    @SerializedName("motivo")
    val motivo: String? = "",

    @SerializedName("consultorio")
    val consultorio: String? = "",

    @SerializedName("doctora")
    val doctora: String? = "",

    @SerializedName("estado")
    val estado: String? = "",

    @SerializedName("codigo_qr")
    val codigoQr: String? = ""
)
data class CancelarCitaRequestN(
    @SerializedName("id_cita")
    val idCita: Int
)
data class CancelarCitaResponseN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = ""
)
