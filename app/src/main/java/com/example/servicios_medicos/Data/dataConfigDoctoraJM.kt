package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataPerfilConfigDoctoraJM(
    @SerializedName("correo") val correo: String? = null,
    @SerializedName("nombre_contacto") val nombreContacto: String? = null,
    @SerializedName("parentesco") val parentesco: String? = null,
    @SerializedName("telefono_contacto") val telefonoContacto: String? = null,
    @SerializedName("recordatorios_citas") val recordatoriosCitas: Boolean? = null,
    @SerializedName("avisos_campanias") val avisosCampanias: Boolean? = null
)

data class dataConfigDoctoraRespuestaJM(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: dataPerfilConfigDoctoraJM? = null
)

data class dataPreferenciasDoctoraJM(
    @SerializedName("recordatorios_citas") val recordatoriosCitas: Boolean,
    @SerializedName("avisos_campanias") val avisosCampanias: Boolean
)

data class dataOperacionDoctoraJM(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("message") val message: String? = null
)