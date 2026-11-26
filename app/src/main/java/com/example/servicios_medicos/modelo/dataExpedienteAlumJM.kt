package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataExpedienteAlumno(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: ExpedienteAlumnoData? = null
)

data class ExpedienteAlumnoData(
    @SerializedName("expediente")
    val expediente: ExpedienteDetalleModelo? = null,

    @SerializedName("consultas_recientes")
    val consultasRecientes: List<ConsultaRecienteModelo> = emptyList()
)

data class ExpedienteDetalleModelo(
    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("fecha_actualizacion")
    val fechaActualizacion: String = "",

    @SerializedName("tipo_sangre")
    val tipoSangre: String = "",

    @SerializedName("alergias")
    val alergias: String = "",

    @SerializedName("padecimientos")
    val padecimientos: String = "",

    @SerializedName("cirugias")
    val cirugias: String = "",

    @SerializedName("tratamiento_habitual")
    val tratamientoHabitual: String = ""
)

data class ConsultaRecienteModelo(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("medico")
    val medico: String = "",

    @SerializedName("estado")
    val estado: String = "",

    @SerializedName("id_receta")
    val idReceta: Int? = null
)

data class dataHistorialConsultas(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val consultas: List<ConsultaRecienteModelo> = emptyList()
)

data class dataRecetaConsulta(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val receta: RecetaDetalleModelo? = null
)

data class RecetaDetalleModelo(
    @SerializedName("id_receta")
    val idReceta: Int = 0,

    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("nombre_paciente")
    val nombrePaciente: String = "",

    @SerializedName("matricula_carrera_cuatrimestre")
    val matriculaCarreraCuatrimestre: String = "",

    @SerializedName("fecha_medico")
    val fechaMedico: String = "",

    @SerializedName("alergia_registrada")
    val alergiaRegistrada: String = "",

    @SerializedName("indicaciones")
    val indicaciones: String = "",

    @SerializedName("medicamentos")
    val medicamentos: String = "",

    @SerializedName("lista_medicamentos")
    val listaMedicamentos: List<MedicamentoRecetaJM> = emptyList(),

    @SerializedName("url_pdf")
    val urlPdf: String = ""
)