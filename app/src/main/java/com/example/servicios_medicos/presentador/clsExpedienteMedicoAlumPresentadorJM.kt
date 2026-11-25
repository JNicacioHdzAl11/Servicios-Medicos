package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.ConsultaItemJM
import com.example.servicios_medicos.Data.dataExpedienteJM
import com.example.servicios_medicos.modelo.clsExpedienteAlumModeloJM
import com.example.servicios_medicos.vista.ExpedienteAlumActivityJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsExpedienteAlumPresentadorJM(private val vista: ExpedienteAlumActivityJM) {
    private val modelo = clsExpedienteAlumModeloJM()
    private var token = ""
    private var matricula = ""
    private var consultasRecientes: List<ConsultaItemJM> = emptyList()

    fun iniciarPantalla(tokenSesion: String?, matriculaAlumno: String?) { // guarda el token y la matricula de sesion
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        if (matriculaAlumno != null && matriculaAlumno.isNotEmpty()) {
            matricula = matriculaAlumno
        }
    }

    fun cargarExpedienteAlumno() { // pide los datos del expediente sin congelar la pantalla
        if (token.isEmpty() || matricula.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerExpedientePorMatricula(token, matricula)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    mostrarDatos(datos)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar el expediente. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun consultarRecetaAnterior(indice: Int) { // pide la receta de una consulta previa
        val consulta = modelo.obtenerConsultaPorIndice(consultasRecientes, indice)
        if (token.isEmpty() || consulta == null || consulta.idConsulta <= 0) {
            vista.mostrarError("Sin receta registrada para esta consulta.")
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerRecetaAnterior(token, consulta.idConsulta)
                val cuerpo = respuesta.body()
                if (respuesta.isSuccessful && cuerpo != null && cuerpo.status && cuerpo.data != null) {
                    val receta = cuerpo.data
                    vista.mostrarRecetaAnterior(modelo.formatearResumenReceta(receta.medicamentos, receta.indicaciones))
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar la receta. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarDatos(datos: dataExpedienteJM) { // manda a la vista los datos del alumno y antecedentes
        val contenido = datos.data
        if (contenido == null) {
            return
        }

        val exp = contenido.expediente
        if (exp != null) {
            if (exp.matricula.isNotEmpty()) {
                matricula = exp.matricula
            }
            vista.mostrarIniciales(modelo.armarIniciales(exp.nombreCompleto))
            vista.mostrarNombrePaciente(exp.nombreCompleto)
            vista.mostrarMatriculaEdad(modelo.formatearMatriculaEdad(exp.matricula, exp.edad))
            vista.mostrarCarreraGrupo(exp.carreraGrupo)
            vista.mostrarAlertaAlergia(modelo.formatearAlertaAlergia(exp.alergias))
            vista.mostrarResumenClinico(
                modelo.resolverTexto(exp.tipoSangre, "No registrado"),
                modelo.resolverTexto(exp.enfermedadesCronicas, "Ninguna referida")
            )
            vista.mostrarAntecedentes(
                modelo.resolverTexto(exp.alergenosDetalle.ifEmpty { exp.alergias }, "Ninguno"),
                modelo.resolverTexto(exp.cirugias, "Sin antecedentes"),
                modelo.resolverTexto(exp.tratamientoHabitual, "Ninguno referido")
            )
        }

        consultasRecientes = contenido.consultasRecientes
    }
}