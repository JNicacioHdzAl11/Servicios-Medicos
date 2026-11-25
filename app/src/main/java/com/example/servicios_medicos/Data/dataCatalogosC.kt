package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class dataOpcionCatalogoC(
    val id: Int,
    val nombre: String
) {

    override fun toString(): String {
        return nombre
    }
}

data class dataCatalogosC(
    val carreras: List<dataOpcionCatalogoC>,
    val grupos: List<dataOpcionCatalogoC>,
    val cuatrimestres: List<dataOpcionCatalogoC>
)

data class dataCatalogosRespuestaC(

    @SerializedName("status")
    val estado: Boolean = false,

    @SerializedName("message")
    val mensaje: String = "",

    @SerializedName("data")
    val datos: dataCatalogosC? = null
)