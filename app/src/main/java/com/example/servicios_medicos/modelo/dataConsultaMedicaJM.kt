package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataConsultaMedicaJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: ConsultaMedicaData? = null
)

data class ConsultaMedicaData(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("nombre_paciente")
    val nombrePaciente: String = "",

    @SerializedName("detalle_alumno")
    val detalleAlumno: String = "",

    @SerializedName("alergia")
    val alergia: String = "",

    @SerializedName("registro_signos")
    val registroSignos: String = "",

    @SerializedName("presion_arterial")
    val presionArterial: String = "",

    @SerializedName("temperatura")
    val temperatura: String = "",

    @SerializedName("frecuencia_cardiaca")
    val frecuenciaCardiaca: String = "",

    @SerializedName("spo2")
    val spo2: String = "",

    @SerializedName("motivo_consulta")
    val motivoConsulta: String = "",

    @SerializedName("padecimiento_actual")
    val padecimientoActual: String = "",

    @SerializedName("sintomas_manifestados")
    val sintomasManifestados: String = "",

    @SerializedName("diagnostico_medico")
    val diagnosticoMedico: String = ""
)

data class PeticionGuardarConsulta(
    @SerializedName("id_consulta")
    val idConsulta: Int,

    @SerializedName("matricula")
    val matricula: String,

    @SerializedName("motivo_consulta")
    val motivoConsulta: String,

    @SerializedName("padecimiento_actual")
    val padecimientoActual: String,

    @SerializedName("sintomas_manifestados")
    val sintomasManifestados: String,

    @SerializedName("diagnostico_medico")
    val diagnosticoMedico: String
)

data class dataRespuestaConsulta(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("id_consulta")
    val idConsulta: Int? = null
)