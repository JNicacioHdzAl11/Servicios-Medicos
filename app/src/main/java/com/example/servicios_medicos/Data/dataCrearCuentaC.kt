package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataCrearCuentaC(
    val nombre: String,
    val matricula: String,
    val correo: String,
    val contrasena: String,
    val confirmar_contrasena: String,
    val id_area: Int,
    val id_grupo: Int,
    val id_cuatrimestre: Int
)

data class dataCrearCuentaRespuestaC(

    @SerializedName("status")
    val estado: Boolean = false,

    @SerializedName("message")
    val mensaje: String = ""
)