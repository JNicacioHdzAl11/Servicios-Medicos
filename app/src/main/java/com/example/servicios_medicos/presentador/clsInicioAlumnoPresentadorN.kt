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
    private var token = ""

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la fecha de hoy
        token = tokenSesion ?: ""
        if (token.isEmpty()) {
            vista.mostrarMensaje("Tu sesión terminó, vuelve a entrar")
        }
        val fecha = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX")).format(Date())
        vista.mostrarFechaHoy(fecha.substring(0, 1).uppercase() + fecha.substring(1))
    }

    fun cargarDatosInicio() { // pide los datos de inicio y las citas y los manda a la vista
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val datos = modelo.obtenerDatosInicio(token)
                val citas = modelo.obtenerCitas(token)
                vista.mostrarResumen(modelo.contarCitasProximas(citas).toString(), "Activo", modelo.calcularPendientes(citas))

                val contenido = datos.data
                if (contenido != null) {
                    val alumno = contenido.alumno
                    if (alumno != null) {
                        vista.mostrarAlumno("¡Hola, " + alumno.nombreCompleto + "!", "Matrícula: " + alumno.matricula)
                    }

                    val cita = modelo.buscarProximaCita(citas)
                    if (cita != null) {
                        val fechaServidor = cita.fecha ?: ""
                        var hora = cita.hora ?: ""
                        if (hora.length > 5) {
                            hora = hora.substring(0, 5)
                        }
                        var fecha = fechaServidor
                        if (fechaServidor.length == 10) {
                            val fechaCita = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(fechaServidor)
                            fecha = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX")).format(fechaCita)
                            fecha = fecha.substring(0, 1).uppercase() + fecha.substring(1)
                        }
                        vista.mostrarProximaCita(cita.motivo ?: "Cita médica", "Pendiente", "Fecha: " + fecha, "Hora: " + hora + " a " + modelo.sumarMediaHora(hora))
                    } else {
                        vista.mostrarProximaCita("Sin citas programadas", "Sin cita", "Sin fecha", "Sin horario asignado")
                    }

                    val campanias = contenido.campaniasActivas
                    for (i in 0 until 2) {
                        if (campanias.size > i) {
                            val campania = campanias[i]
                            vista.mostrarCampania(i + 1, campania.titulo, "Lugar: " + campania.lugar + " (" + campania.fechaInicio + " - " + campania.fechaFin + ")")
                        } else {
                            vista.mostrarCampania(i + 1, "Sin campañas", "No hay campañas por ahora")
                        }
                    }
                }
            } catch (e: Exception) {
                vista.mostrarMensaje("Error al cargar los datos")
            }
        }
    }
    fun regresarAlLogin() { // regresa al login
        vista.abrirLogin()
    }

    fun verConfiguracion() { // abre configuracion
        vista.abrirConfiguracion()
    }

    fun solicitarCita() { // abre agendar cita
        vista.abrirAgendarCita(token)
    }

    fun verTodasLasCitas() { // abre mis citas
        vista.abrirMisCitas()
    }

    fun verCampanias() { // abre campañas de salud
        vista.abrirCampanias()
    }
}
