package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataLoginC(
    val correo: String,
    val contrasena: String
)

data class dataUsuarioC(
    @SerializedName("id_usuario")
    val idUsuario: Int = 0,
    val correo: String = "",
    val rol: String = "",
    val identificador: String = "",
    val token: String = ""
)

data class dataLoginRespuestaC(
    @SerializedName("status")
    val estado: Boolean = false,
    @SerializedName("message")
    val mensaje: String = "",
    @SerializedName("data")
    val datos: dataUsuarioC? = null
)
