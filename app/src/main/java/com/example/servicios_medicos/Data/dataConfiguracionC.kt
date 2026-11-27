package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataPerfilConfiguracionC(

    val correo: String = "",

    @SerializedName("nombre_contacto")
    val nombreContacto: String = "",

    val parentesco: String = "",

    @SerializedName("telefono_contacto")
    val telefonoContacto: String = "",

    @SerializedName("recordatorios_citas")
    val recordatoriosCitas: Boolean = false,

    @SerializedName("avisos_campanias")
    val avisosCampanias: Boolean = false
)

data class dataPerfilRespuestaC(

    @SerializedName("status")
    val estado: Boolean = false,

    @SerializedName("message")
    val mensaje: String = "",

    @SerializedName("data")
    val datos: dataPerfilConfiguracionC? = null
)

data class dataOperacionRespuestaC(

    @SerializedName("status")
    val estado: Boolean = false,

    @SerializedName("message")
    val mensaje: String = ""
)

data class dataActualizarContrasenaC(

    @SerializedName("contrasena_actual")
    val contrasenaActual: String,

    @SerializedName("contrasena_nueva")
    val contrasenaNueva: String,

    @SerializedName("confirmar_contrasena")
    val confirmarContrasena: String
)

data class dataContactoEmergenciaC(

    @SerializedName("nombre_contacto")
    val nombreContacto: String,

    val parentesco: String,

    @SerializedName("telefono_contacto")
    val telefonoContacto: String
)

data class dataPreferenciasConfiguracionC(

    @SerializedName("recordatorios_citas")
    val recordatoriosCitas: Boolean,

    @SerializedName("avisos_campanias")
    val avisosCampanias: Boolean
)