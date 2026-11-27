package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataInicioDoctoraJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: InicioDoctoraDataJM? = null
)

data class InicioDoctoraDataJM(
    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",

    @SerializedName("cedula")
    val cedula: String = "",

    @SerializedName("fecha_sesion")
    val fechaSesion: String = "",

    @SerializedName("citas_hoy")
    val citasHoy: Int = 0,

    @SerializedName("consultas_realizadas")
    val consultasRealizadas: Int = 0,

    @SerializedName("matricula_espera")
    val matriculaEspera: String = "",

    @SerializedName("nombre_espera")
    val nombreEspera: String = "",

    @SerializedName("detalle_espera")
    val detalleEspera: String = "",

    @SerializedName("motivo_espera")
    val motivoEspera: String = "",

    @SerializedName("signos_espera")
    val signosEspera: String = "",

    @SerializedName("proxima_1")
    val proxima1: String = "",

    @SerializedName("hora_1")
    val hora1: String = "",

    @SerializedName("proxima_2")
    val proxima2: String = "",

    @SerializedName("hora_2")
    val hora_2: String = ""
)

data class dataExpedienteDoctoraJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: ExpedienteDoctoraDataJM? = null
)

data class ExpedienteDoctoraDataJM(
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

    @SerializedName("consulta_1")
    val consulta1: String = "",

    @SerializedName("consulta_2")
    val consulta2: String = ""
)

data class dataConfigDoctoraJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: ConfigDoctoraDataJM? = null
)

data class ConfigDoctoraDataJM(
    @SerializedName("correo_institucional")
    val correoInstitucional: String = "",

    @SerializedName("cedula_firma")
    val cedulaFirma: String = "",

    @SerializedName("modo_oscuro")
    val modoOscuro: Boolean = false,

    @SerializedName("avisos_citas")
    val avisosCitas: Boolean = true
)

data class PeticionConfigDoctoraJM(
    @SerializedName("modo_oscuro")
    val modoOscuro: Boolean,

    @SerializedName("avisos_citas")
    val avisosCitas: Boolean
)