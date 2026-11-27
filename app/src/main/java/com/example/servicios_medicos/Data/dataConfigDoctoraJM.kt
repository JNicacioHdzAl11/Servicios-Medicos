package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataConfigDoctoraRespuestaJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: dataPerfilConfigDoctoraJM? = null
)

data class dataPerfilConfigDoctoraJM(
    @SerializedName("correo") val correo: String = "",
    @SerializedName("cedula_firma") val cedulaFirma: String = "",
    @SerializedName("modo_oscuro") val modoOscuro: Boolean = false,
    @SerializedName("avisos_urgencias") val avisosUrgencias: Boolean = true
)

data class dataPreferenciasDoctoraJM(
    @SerializedName("modo_oscuro") val modoOscuro: Boolean,
    @SerializedName("avisos_urgencias") val avisosUrgencias: Boolean
)

data class dataOperacionDoctoraJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = ""
)