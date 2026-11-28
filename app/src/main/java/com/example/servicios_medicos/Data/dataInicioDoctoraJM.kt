package com.example.servicios_medicos.Data

data class DoctoraPerfilJM(
    val nombreCompleto: String? = null,
    val cedula: String? = null,
    val correo: String? = null
)

data class ResumenConsultasDoctoraJM(
    val citasHoy: Int = 0,
    val atendidasHoy: Int = 0
)

data class ConsultaEsperaDoctoraJM(
    val matricula: String = "",
    val nombreAlumno: String = "",
    val fecha: String = "",
    val hora: String = "",
    val horaSignos: String? = null,
    val motivo: String = "",
    val presion: String? = null,
    val temperatura: String? = null,
    val frecuenciaCardiaca: String? = null,
    val saturacionOxigeno: String? = null
)

data class InicioDoctoraDataJM(
    val doctora: DoctoraPerfilJM? = null,
    val resumen: ResumenConsultasDoctoraJM? = null,
    val consultasEnEspera: List<ConsultaEsperaDoctoraJM> = emptyList()
)

data class dataInicioDoctoraJM(
    val status: Boolean? = null,
    val data: InicioDoctoraDataJM? = null,
    val message: String? = null
)