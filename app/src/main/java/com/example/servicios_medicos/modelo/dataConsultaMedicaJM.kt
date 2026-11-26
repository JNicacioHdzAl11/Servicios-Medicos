package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class PeticionConsultaJM(
    @SerializedName("id_cita")
    val idCita: Int = 0,

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("motivo_consulta")
    val motivoConsulta: String = "",

    @SerializedName("padecimiento_actual")
    val padecimientoActual: String = "",

    @SerializedName("sintomas_manifestados")
    val sintomasManifestados: String = "",

    @SerializedName("diagnostico_medico")
    val diagnosticoMedico: String = ""
)

data class dataDetalleConsultaJM(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("nombre_alumno")
    val nombreAlumno: String = "",

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

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("motivo_consulta")
    val motivoConsulta: String = "",

    @SerializedName("padecimiento_actual")
    val padecimientoActual: String = "",

    @SerializedName("sintomas_manifestados")
    val sintomasManifestados: String = "",

    @SerializedName("diagnostico_medico")
    val diagnosticoMedico: String = "",

    @SerializedName("id_receta")
    val idReceta: Int? = null
)

data class dataRespuestaP3JM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("id_consulta")
    val idConsulta: Int? = null,

    @SerializedName("id_receta")
    val idReceta: Int? = null,

    @SerializedName("data")
    val data: dataDetalleConsultaJM? = null
)