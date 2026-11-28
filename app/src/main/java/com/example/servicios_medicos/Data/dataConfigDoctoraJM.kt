package com.example.servicios_medicos.Data

data class dataPerfilConfigDoctoraJM(
    val correo: String? = null,
    val cedulaProfesional: String? = null,
    val firmaActiva: Boolean? = null,
    val avisosUrgencias: Boolean? = null,
    val modoOscuro: Boolean? = null
)

data class dataConfigDoctoraRespuestaJM(
    val status: String? = null,
    val data: dataPerfilConfigDoctoraJM? = null,
    val message: String? = null
)

data class dataOperacionDoctoraJM(
    val estado: Boolean? = null,
    val status: String? = null,
    val mensaje: String? = null,
    val message: String? = null
)