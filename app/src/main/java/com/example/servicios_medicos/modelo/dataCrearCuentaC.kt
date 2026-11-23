
package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataCrearCuentaC(
    val nombre: String,
    val matricula: String,
    val correo: String,
    val contrasena: String,
    val confirmar_contrasena: String
)

data class dataCrearCuentaRespuestaC(
    @SerializedName("status")
    val estado: Boolean = false,

    @SerializedName("message")
    val mensaje: String = ""
)
