package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

class dataPerfilJN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: PerfilData? = null
)

class PerfilData(
    @SerializedName("matricula")
    val matricula: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("apellido_p")
    val apellidoP: String = "",

    @SerializedName("apellido_m")
    val apellidoM: String = "",

    @SerializedName("nombre_completo")
    val nombreCompleto: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("area")
    val area: String = "",

    @SerializedName("cuatrimestre")
    val cuatrimestre: Int = 0,

    @SerializedName("grupo")
    val grupo: String = "",

    @SerializedName("tipo_sangre")
    val tipoSangre: String = "",

    @SerializedName("edad")
    val edad: String = "",

    @SerializedName("sexo")
    val sexo: String = "",

    @SerializedName("correo")
    val correo: String = "",

    @SerializedName("contacto_emergencia")
    val contactoEmergencia: ContactoEmergenciaModelo? = null
)

class ContactoEmergenciaModelo(
    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("parentesco")
    val parentesco: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("telefono_alterno")
    val telefonoAlterno: String? = null
)