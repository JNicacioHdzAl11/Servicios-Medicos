package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

class dataDetalleCampaniaResponseJN(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("data")
    val data: dataDetalleCampaniaJN? = null
)

class dataDetalleCampaniaJN(
    @SerializedName("id_campania")
    val idCampania: Int = 0,

    @SerializedName("titulo")
    val titulo: String = "",

    @SerializedName("categoria")
    val categoria: String? = null,

    @SerializedName("lema")
    val lema: String? = null,

    @SerializedName("descripcion")
    val descripcion: String = "",

    @SerializedName("requisitos")
    val requisitos: String? = null,

    @SerializedName("recomendacion_antes")
    val recomendacionAntes: String? = null,

    @SerializedName("recomendacion_despues")
    val recomendacionDespues: String? = null,

    @SerializedName("fecha_inicio")
    val fechaInicio: String = "",

    @SerializedName("fecha_fin")
    val fechaFin: String = "",

    @SerializedName("horario")
    val horario: String = "",

    @SerializedName("lugar")
    val lugar: String = "",

    @SerializedName("imagen_url")
    val imagenUrl: String? = null,

    @SerializedName("infografia_url")
    val infografiaUrl: String? = null,

    @SerializedName("etiquetas")
    val etiquetas: List<String> = emptyList()
)