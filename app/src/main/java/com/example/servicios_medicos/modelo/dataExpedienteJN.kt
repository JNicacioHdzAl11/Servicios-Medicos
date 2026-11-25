package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

// --- Respuesta de expediente/obtener_expediente.php ---
class dataExpedienteResponseJ(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: dataExpedienteContenedorJ? = null
)

class dataExpedienteContenedorJ(
    @SerializedName("expediente")
    val expediente: dataExpedienteJN? = null,

    @SerializedName("total_consultas")
    val totalConsultas: Int = 0,

    @SerializedName("ultima_solicitud")
    val ultimaSolicitud: dataSolicitudExpedienteJ? = null
)

class dataExpedienteJN(
    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",

    @SerializedName("id_expediente")
    val idExpediente: Int = 0,

    @SerializedName("curp")
    val curp: String? = null,

    @SerializedName("nss")
    val nss: String? = null,

    @SerializedName("edad")
    val edad: Int? = null,

    @SerializedName("sexo")
    val sexo: String? = null,

    @SerializedName("tipo_sangre")
    val tipoSangre: String? = null,

    @SerializedName("talla")
    val talla: String? = null,

    @SerializedName("peso")
    val peso: String? = null,

    @SerializedName("alergias")
    val alergias: String? = null,

    @SerializedName("enfermedades")
    val enfermedades: String? = null,

    @SerializedName("antecedentes")
    val antecedentes: String? = null,

    @SerializedName("revision_ocular")
    val revisionOcular: String? = null,

    @SerializedName("motivo_consulta")
    val motivoConsulta: String? = null
)

class dataSolicitudExpedienteJ(
    @SerializedName("id_solicitud")
    val idSolicitud: Int = 0,

    @SerializedName("motivo")
    val motivo: String? = null,

    @SerializedName("formato")
    val formato: String? = null,

    @SerializedName("estado")
    val estado: String? = null,

    @SerializedName("fecha_solicitud")
    val fechaSolicitud: String? = null,

    @SerializedName("fecha_respuesta")
    val fechaRespuesta: String? = null
)

// --- Respuesta de expediente/historial_consultas.php ---
class dataHistorialConsultasResponseJ(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<dataConsultaJ> = emptyList()
)

class dataConsultaJ(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("fecha_consulta")
    val fechaConsulta: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("presion")
    val presion: String? = null,

    @SerializedName("temperatura")
    val temperatura: String? = null,

    @SerializedName("peso")
    val peso: String? = null,

    @SerializedName("talla")
    val talla: String? = null,

    @SerializedName("frecuencia_cardiaca")
    val frecuenciaCardiaca: String? = null,

    @SerializedName("id_diagnostico")
    val idDiagnostico: Int? = null,

    @SerializedName("diagnostico")
    val diagnostico: String? = null,

    @SerializedName("doctora")
    val doctora: String? = null,

    @SerializedName("id_receta")
    val idReceta: Int? = null,

    @SerializedName("tiene_receta")
    val tieneReceta: Boolean = false
)
