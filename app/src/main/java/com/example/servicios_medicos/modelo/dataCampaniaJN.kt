package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

class dataCampaniaResponseJN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: List<dataCampaniaJN>? = null
)

class dataCampaniaJN(
    @SerializedName("id_campania")
    val idCampania: Int = 0,

    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("fecha_inicio")
    val fechaInicio: String = "",

    @SerializedName("fecha_fin")
    val fechaFin: String = "",

    @SerializedName("horario")
    val horario: String = "",

    @SerializedName("lugar")
    val lugar: String = "",

    @SerializedName("imagen_url")
    val imagenUrl: String? = null
)