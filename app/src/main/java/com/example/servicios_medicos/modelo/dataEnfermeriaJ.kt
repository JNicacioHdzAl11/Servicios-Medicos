package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataRespuestaEnfermeriaJ<T>(
    @SerializedName("status") val estado: Boolean = false,
    @SerializedName("message") val mensaje: String = "",
    @SerializedName("data") val datos: T? = null
)

data class dataAlumnoEnfermeriaJ(
    val matricula: String = "",
    @SerializedName("nombre_completo") val nombre: String = "",
    val area: String? = null,
    val cuatrimestre: Int? = null,
    val grupo: String? = null,
    @SerializedName("tiene_cita") val tieneCita: Boolean? = null
) {
    fun informacionAcademica(): String {
        val datos = ArrayList<String>()
        if (!area.isNullOrBlank()) datos.add(area)
        if (cuatrimestre != null) datos.add("Cuatrimestre $cuatrimestre")
        if (!grupo.isNullOrBlank()) datos.add("Grupo $grupo")
        if (datos.isEmpty()) return "Sin datos académicos registrados"
        return datos.joinToString(" · ")
    }
}

data class dataCitaEnfermeriaJ(
    @SerializedName("id_cita") val idCita: Int = 0,
    val matricula: String = "",
    @SerializedName("nombre_alumno") val nombre: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = ""
)

data class dataSignosEnfermeriaJ(
    @SerializedName("id_cita") val idCita: Int?,
    val matricula: String,
    val temperatura: Double?,
    @SerializedName("frecuencia_cardiaca") val frecuenciaCardiaca: Int?,
    val presion: String?,
    @SerializedName("saturacion_oxigeno") val saturacion: Int?,
    val peso: Double?,
    @SerializedName("frecuencia_respiratoria") val frecuenciaRespiratoria: Int?,
    val observaciones: String
)
