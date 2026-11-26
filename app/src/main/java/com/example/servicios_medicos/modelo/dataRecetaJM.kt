package com.example.servicios_medicos.modelo

import com.google.gson.annotations.SerializedName

data class MedicamentoRecetaJM(
    @SerializedName("id_medicamento")
    val idMedicamento: Int = 0,

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("presentacion_via")
    val presentacionVia: String = "",

    @SerializedName("dosis")
    val dosis: String = "",

    @SerializedName("frecuencia")
    val frecuencia: String = "",

    @SerializedName("duracion")
    val duracion: String = "",

    @SerializedName("nota")
    val nota: String = "",

    @SerializedName("cantidad")
    val cantidad: Int = 1
)

data class PeticionRecetaJM(
    @SerializedName("id_consulta")
    val idConsulta: Int = 0,

    @SerializedName("indicaciones")
    val indicaciones: String = "",

    @SerializedName("medicamentos")
    val medicamentos: List<MedicamentoRecetaJM> = emptyList(),

    @SerializedName("estado")
    val estado: String = "Emitida"
)

data class dataRecetaJM(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("id_receta")
    val idReceta: Int? = null,

    @SerializedName("data")
    val data: RecetaDetalleModelo? = null
)