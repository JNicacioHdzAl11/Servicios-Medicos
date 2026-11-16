package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataCampaniaN(
    @SerializedName("id_campania") val idCampania: Int = 0,
    val titulo: String = "",
    val descripcion: String? = null,
    @SerializedName("fecha_inicio") val fechaInicio: String = "",
    @SerializedName("fecha_fin") val fechaFin: String = "",
    val lugar: String = "",
    @SerializedName("imagen_url") val imagenUrl: String? = null,
    val requisitos: String? = null
)