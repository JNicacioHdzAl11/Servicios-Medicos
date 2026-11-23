package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsInicioAlumnoModeloN
import com.example.servicios_medicos.vista.InicioAlumnoFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class clsInicioAlumnoPresentadorN(private val vista: InicioAlumnoFragmentN) {
    private val modelo = clsInicioAlumnoModeloN()
    fun iniciarPantalla(token: String?, nombre: String?, matricula: String?) { // guarda el token y muestra los datos iniciales
        if (nombre != null && nombre.isNotEmpty()) {
            vista.mostrarSaludo("¡Hola, $nombre!")
        } else {
            vista.mostrarSaludo("¡Hola, Alumno!")
        }

        if (matricula != null && matricula.isNotEmpty()) {
            vista.mostrarMatricula("Matrícula: $matricula")
        } else {
            vista.mostrarMatricula("Matrícula: Sin registro")
        }
        val fechaHoy = obtenerFechaHoy()
        vista.mostrarFechaHoy(fechaHoy)

        if (token != null && token.isNotEmpty()) {
            cargarDatosInicio(token)
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
    }
    fun cargarDatosInicio(token: String) { // pide los datos de inicio al modelo y los manda a la vista
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.obtenerDatosInicio(token)

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()

                    if (datos != null && datos.status) {
                        val infoAlumno = modelo.obtenerInfoAlumno(datos)
                        if (infoAlumno.size == 2) {
                            if (infoAlumno[0].isNotEmpty()) {
                                vista.mostrarSaludo("¡Hola, " + infoAlumno[0] + "!")
                            }
                            if (infoAlumno[1].isNotEmpty()) {
                                vista.mostrarMatricula("Matrícula: " + infoAlumno[1])
                            }
                        }

                        val totalCitas = modelo.calcularTotalCitas(token)
                        vista.mostrarResumen(totalCitas.toString(), "Activo")

                        val cita = modelo.obtenerProximaCita(datos)
                        vista.mostrarProximaCita(cita[0], cita[1], cita[2], cita[3])

                        val campania1 = modelo.obtenerCampania1(datos)
                        vista.mostrarCampania1(campania1[0], campania1[1])

                        val campania2 = modelo.obtenerCampania2(datos)
                        vista.mostrarCampania2(campania2[0], campania2[1])
                    } else {
                        vista.mostrarError("No pudimos cargar tu información. Intenta de nuevo.")
                    }
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("El servicio no está disponible. Intenta más tarde.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }
    private fun obtenerFechaHoy(): String { // arma el texto de la fecha de hoy
        val formato = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX"))
        val fecha = formato.format(Date())
        val primeraLetra = fecha.substring(0, 1).uppercase()
        val restoTexto = fecha.substring(1)
        return primeraLetra + restoTexto
    }
}
