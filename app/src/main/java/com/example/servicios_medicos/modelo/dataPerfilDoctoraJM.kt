package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

data class dataPerfilDoctoraJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: PerfilDetalleJM? = null
)

data class PerfilDetalleJM(
    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",

    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("rol_cuenta")
    val rolCuenta: String = "",

    @SerializedName("carrera")
    val carrera: String = "",

    @SerializedName("grupo")
    val grupo: String = "",

    @SerializedName("turno")
    val turno: String = "",

    @SerializedName("semestre")
    val semestre: String = "",

    @SerializedName("edad")
    val edad: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("correo")
    val correo: String = "",

    @SerializedName("contacto_emergencia")
    val contactoEmergencia: String = "",

    @SerializedName("parentesco_emergencia")
    val parentescoEmergencia: String = "",

    @SerializedName("telefono_emergencia")
    val telefonoEmergencia: String = ""
)