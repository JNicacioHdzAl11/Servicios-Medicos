package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

class dataConsultaJN(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("fecha_consulta")
    val fechaConsulta: String = "",

    @SerializedName("motivo")
    val motivo: String = "",

    @SerializedName("presion")
    val presion: String? = null,

    @SerializedName("temperatura")
    val temperatura: String? = null,

    @SerializedName("peso")
    val peso: String? = null,

    @SerializedName("talla")
    val talla: String? = null,

    @SerializedName("frecuencia_cardiaca")
    val frecuenciaCardiaca: String? = null,

    @SerializedName("id_diagnostico")
    val idDiagnostico: Int? = null,

    @SerializedName("diagnostico")
    val diagnostico: String? = null,

    @SerializedName("doctora")
    val doctora: String? = null,

    @SerializedName("id_receta")
    val idReceta: Int? = null,

    @SerializedName("tiene_receta")
    val tieneReceta: Boolean = false
)
