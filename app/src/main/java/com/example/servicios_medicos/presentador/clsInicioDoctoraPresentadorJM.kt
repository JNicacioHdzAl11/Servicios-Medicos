package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.modelo.clsInicioDoctoraModeloJM
import com.example.servicios_medicos.vista.InicioDoctoraFragmentJM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsInicioDoctoraPresentadorJM(private val vista: InicioDoctoraFragmentJM) {
    private val modelo = clsInicioDoctoraModeloJM()
    private var token = ""

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la fecha de hoy
        vista.mostrarFechaHoy(modelo.obtenerFechaHoy())
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
    }

    fun cargarInicioDoctora() { // pide los datos de inicio de la doctora sin congelar la pantalla
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerInicioDoctora(token)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status == true) {
                    mostrarDatos(datos)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar el panel de la doctora. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarDatos(datos: dataInicioDoctoraJM) { // manda a la vista la doctora, el resumen, el paciente en espera y proximas consultas
        val contenido = datos.data
        if (contenido == null) {
            return
        }

        val doctora = contenido.doctora
        if (doctora != null) {
            val nombre = doctora.nombreCompleto.orEmpty()
            val cedula = doctora.cedula.orEmpty()
            vista.mostrarEncabezadoDoctora(
                modelo.armarSaludo(nombre),
                modelo.formatearCedula(cedula),
                modelo.obtenerFechaHoy()
            )
        }

        val resumen = contenido.resumen
        if (resumen != null) {
            vista.mostrarContadores(
                modelo.formatearContador(resumen.citasHoy),
                modelo.formatearContador(resumen.atendidasHoy)
            )
        } else {
            vista.mostrarContadores("00", "00")
        }

        val espera = contenido.consultasEnEspera
        if (espera.isNotEmpty()) {
            val actual = espera[0]
            val signos = listOfNotNull(
                actual.presion?.takeIf { it.isNotBlank() }?.let { "Presión: $it" },
                actual.temperatura?.takeIf { it.isNotBlank() }?.let { "Temp: $it" },
                actual.frecuenciaCardiaca?.takeIf { it.isNotBlank() }?.let { "FC: $it" },
                actual.saturacionOxigeno?.takeIf { it.isNotBlank() }?.let { "SpO₂: $it" }
            ).joinToString(" · ")
            vista.mostrarPacienteEnEspera(
                actual.matricula,
                modelo.obtenerInicialesPaciente(actual.nombreAlumno),
                actual.nombreAlumno,
                "Fecha: " + actual.fecha + " · Hora: " + actual.hora,
                actual.motivo,
                signos
            )
        } else {
            vista.mostrarPacienteEnEspera(
                "",
                "--",
                "Sin pacientes en espera",
                "Sin matrícula ni hora",
                "No hay consultas en espera por ahora",
                "Sin signos vitales registrados"
            )
        }

        val prox1 = if (espera.size > 1) {
            espera[1].nombreAlumno + " · " + espera[1].motivo + " · " + espera[1].hora
        } else {
            "Sin próxima consulta"
        }
        val prox2 = if (espera.size > 2) {
            espera[2].nombreAlumno + " · " + espera[2].motivo + " · " + espera[2].hora
        } else {
            ""
        }
        vista.mostrarProximasConsultas(prox1, prox2)
    }
}