package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataRespuestaN<T>(
    val status: Boolean = false,
    val message: String = "",
    val data: T? = null
)

data class dataAlumnoResumenN(
    val matricula: String = "",
    @SerializedName("nombre_completo") val nombreCompleto: String = "",
    val area: String = "",
    val cuatrimestre: Int = 0,
    val grupo: String = "",
    val telefono: String? = null
)

data class dataCitaN(
    @SerializedName("id_cita") val idCita: Int = 0,
    val fecha: String = "",
    val hora: String = "",
    val area: String? = null,
    val motivo: String = "",
    val estado: String = "",
    @SerializedName("codigo_qr") val codigoQr: String? = null,
    @SerializedName("motivo_cancelacion") val motivoCancelacion: String? = null
)

data class dataInicioAlumnoN(
    val alumno: dataAlumnoResumenN = dataAlumnoResumenN(),
    @SerializedName("proxima_cita") val proximaCita: dataCitaN? = null,
    @SerializedName("campanias_activas") val campaniasActivas: List<dataCampaniaN> = emptyList()
)

data class dataHorariosN(
    val fecha: String = "",
    @SerializedName("horarios_disponibles") val horariosDisponibles: List<String> = emptyList()
)

data class dataAgendarCitaRequestN(
    @SerializedName("id_area") val idArea: Int = 0,
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = ""
)

data class dataCitaAgendadaN(
    @SerializedName("id_cita") val idCita: Int = 0,
    val matricula: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val estado: String = "",
    @SerializedName("codigo_qr") val codigoQr: String = ""
)

data class dataCancelarCitaRequestN(
    @SerializedName("id_cita") val idCita: Int = 0,
    @SerializedName("motivo_cancelacion") val motivoCancelacion: String = ""
)

data class dataCitaCanceladaN(
    @SerializedName("id_cita") val idCita: Int = 0,
    val estado: String = ""
)

data class dataComprobanteQrN(
    @SerializedName("id_cita") val idCita: Int = 0,
    @SerializedName("codigo_qr") val codigoQr: String = "",
    val alumno: String = "",
    val matricula: String = "",
    val fecha: String = "",
    val hora: String = "",
    val area: String? = null,
    val estado: String = ""
)