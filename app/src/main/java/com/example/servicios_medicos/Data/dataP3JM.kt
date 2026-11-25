package com.example.servicios_medicos.Data

import com.google.gson.annotations.SerializedName

// --- PERFIL ---
data class dataPerfilJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: PerfilDetalleJM? = null
)

data class PerfilDetalleJM(
    @SerializedName("id_usuario") val idUsuario: Int = 0,
    @SerializedName("nombre_completo") val nombreCompleto: String = "",
    @SerializedName("matricula") val matricula: String = "",
    @SerializedName("correo") val correo: String = "",
    @SerializedName("telefono") val telefono: String = "",
    @SerializedName("carrera") val carrera: String = "",
    @SerializedName("cuatrimestre") val cuatrimestre: String = "",
    @SerializedName("grupo") val grupo: String = "",
    @SerializedName("contacto_emergencia") val contactoEmergencia: String = "",
    @SerializedName("telefono_emergencia") val telefonoEmergencia: String = ""
)

// --- EXPEDIENTE (ALUMNO Y MÉDICO) ---
data class dataExpedienteJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: ExpedienteContenidoJM? = null
)

data class ExpedienteContenidoJM(
    @SerializedName("expediente") val expediente: ExpedienteDetalleJM? = null,
    @SerializedName("consultas_recientes") val consultasRecientes: List<ConsultaItemJM> = emptyList()
)

data class ExpedienteDetalleJM(
    @SerializedName("nombre_completo") val nombreCompleto: String = "",
    @SerializedName("matricula") val matricula: String = "",
    @SerializedName("edad") val edad: String = "",
    @SerializedName("carrera_grupo") val carreraGrupo: String = "",
    @SerializedName("fecha_actualizacion") val fechaActualizacion: String = "",
    @SerializedName("tipo_sangre") val tipoSangre: String = "",
    @SerializedName("alergias") val alergias: String = "",
    @SerializedName("alergenos_detalle") val alergenosDetalle: String = "",
    @SerializedName("enfermedades_cronicas") val enfermedadesCronicas: String = "",
    @SerializedName("padecimientos") val padecimientos: String = "",
    @SerializedName("cirugias") val cirugias: String = "",
    @SerializedName("tratamiento_habitual") val tratamientoHabitual: String = ""
)

data class ConsultaItemJM(
    @SerializedName("id_consulta") val idConsulta: Int = 0,
    @SerializedName("fecha") val fecha: String = "",
    @SerializedName("motivo") val motivo: String = "",
    @SerializedName("diagnostico") val diagnostico: String = "",
    @SerializedName("medico") val medico: String = "",
    @SerializedName("estado") val estado: String = "",
    @SerializedName("id_receta") val idReceta: Int? = null
)

// --- CONSULTA Y RECETA ---
data class dataDetalleConsultaJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: DetalleConsultaJM? = null
)

data class DetalleConsultaJM(
    @SerializedName("id_consulta") val idConsulta: Int = 0,
    @SerializedName("matricula") val matricula: String = "",
    @SerializedName("nombre_alumno") val nombreAlumno: String = "",
    @SerializedName("detalle_alumno") val detalleAlumno: String = "",
    @SerializedName("alergia") val alergia: String = "",
    @SerializedName("registro_signos") val registroSignos: String = "",
    @SerializedName("presion_arterial") val presionArterial: String = "",
    @SerializedName("temperatura") val temperatura: String = "",
    @SerializedName("frecuencia_cardiaca") val frecuenciaCardiaca: String = "",
    @SerializedName("spo2") val spo2: String = "",
    @SerializedName("motivo_consulta") val motivoConsulta: String = "",
    @SerializedName("padecimiento_actual") val padecimientoActual: String = "",
    @SerializedName("sintomas_manifestados") val sintomasManifestados: String = "",
    @SerializedName("diagnostico_medico") val diagnosticoMedico: String = ""
)
data class PeticionConsultaJM(
    @SerializedName("id_cita") val idCita: Int,
    @SerializedName("matricula") val matricula: String,
    @SerializedName("motivo_consulta") val motivoConsulta: String,
    @SerializedName("padecimiento_actual") val padecimientoActual: String,
    @SerializedName("sintomas_manifestados") val sintomasManifestados: String,
    @SerializedName("diagnostico_medico") val diagnosticoMedico: String
)

data class MedicamentoRecetaJM(
    @SerializedName("id_medicamento") val idMedicamento: Int = 0,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("presentacion_via") val presentacionVia: String = "",
    @SerializedName("dosis") val dosis: String = "",
    @SerializedName("frecuencia") val frecuencia: String = "",
    @SerializedName("duracion") val duracion: String = "",
    @SerializedName("nota") val nota: String = ""
)

data class PeticionRecetaJM(
    @SerializedName("id_consulta") val idConsulta: Int,
    @SerializedName("indicaciones") val indicaciones: String,
    @SerializedName("medicamentos") val medicamentos: List<MedicamentoRecetaJM>,
    @SerializedName("estado") val estado: String
)

data class dataRecetaJM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("data") val data: RecetaDetalleJM? = null
)

data class RecetaDetalleJM(
    @SerializedName("id_receta") val idReceta: Int = 0,
    @SerializedName("id_consulta") val idConsulta: Int = 0,
    @SerializedName("nombre_paciente") val nombrePaciente: String = "",
    @SerializedName("matricula_carrera_cuatrimestre") val matriculaCarreraCuatrimestre: String = "",
    @SerializedName("fecha_medico") val fechaMedico: String = "",
    @SerializedName("alergia_registrada") val alergiaRegistrada: String = "",
    @SerializedName("medicamentos") val medicamentos: String = "",
    @SerializedName("lista_medicamentos") val listaMedicamentos: List<MedicamentoItemJM> = emptyList(),
    @SerializedName("indicaciones") val indicaciones: String = "",
    @SerializedName("url_pdf") val urlPdf: String = ""
)
data class MedicamentoItemJM(
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("presentacion_via") val presentacionVia: String = "",
    @SerializedName("dosis") val dosis: String = "",
    @SerializedName("frecuencia") val frecuencia: String = "",
    @SerializedName("duracion") val duracion: String = "",
    @SerializedName("nota") val nota: String = ""
)
data class dataRespuestaP3JM(
    @SerializedName("status") val status: Boolean = false,
    @SerializedName("message") val message: String = "",
    @SerializedName("id_consulta") val idConsulta: Int? = null,
    @SerializedName("id_receta") val idReceta: Int? = null
)