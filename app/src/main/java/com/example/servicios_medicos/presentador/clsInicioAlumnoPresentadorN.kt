package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsInicioAlumnoModeloN
import com.example.servicios_medicos.Data.dataInicioAlumnoN
import com.example.servicios_medicos.vista.InicioAlumnoFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class clsInicioAlumnoPresentadorN(private val vista: InicioAlumnoFragmentN) {
    private val modelo = clsInicioAlumnoModeloN()
    private var token = ""

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la fecha de hoy
        vista.mostrarFechaHoy(modelo.obtenerFechaHoy())
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
    }

    fun cargarDatosInicio() { // pide los datos de inicio y las citas sin congelar la pantalla
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDatosInicio(token)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    mostrarDatos(datos)
                    val citas = modelo.listarCitas(token)
                    val totalCitas = modelo.contarCitasProximas(citas)
                    val textoPendientes = modelo.calcularPendientes(citas)
                    vista.mostrarResumen(totalCitas.toString(), "Activo", textoPendientes)
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar tu información. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    private fun mostrarDatos(datos: dataInicioAlumnoN) { // manda a la vista el alumno, la proxima cita y las campañas
        val contenido = datos.data
        if (contenido == null) {
            return
        }

        val alumno = contenido.alumno
        if (alumno != null) {
            vista.mostrarSaludo("¡Hola, " + alumno.nombreCompleto + "!")
            vista.mostrarMatricula("Matrícula: " + alumno.matricula)
        }

        val cita = contenido.proximaCita
        if (cita != null) {
            vista.mostrarProximaCita(cita.motivo, cita.estado, "Fecha: " + cita.fecha, "Hora: " + cita.hora)
        } else {
            vista.mostrarProximaCita("Sin citas programadas", "Sin cita", "Sin fecha", "Sin horario asignado")
        }

        val campanias = contenido.campaniasActivas
        for (i in 0 until 2) {
            if (campanias.size > i) {
                val campania = campanias[i]
                val detalle = "Lugar: " + campania.lugar + " (" + campania.fechaInicio + " - " + campania.fechaFin + ")"
                vista.mostrarCampania(i + 1, campania.titulo, detalle)
            } else {
                vista.mostrarCampania(i + 1, "Sin campañas", "No hay campañas por ahora")
            }
        }
    }
}

