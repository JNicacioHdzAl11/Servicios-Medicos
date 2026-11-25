package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.CitaDetalleN
import com.example.servicios_medicos.modelo.TarjetaCitaN
import com.example.servicios_medicos.modelo.clsCitasModeloN
import com.example.servicios_medicos.vista.CitasFragmentN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class clsCitasPresentadorN(private val vista: CitasFragmentN) {
    private val modelo = clsCitasModeloN()
    private var token = ""
    private var pestania = "proximas"
    private var fechaElegida = ""
    private var lunesSemana = modelo.buscarLunesDeLaSemana(Calendar.getInstance())
    private var todasLasCitas: List<CitaDetalleN> = emptyList()
    private var citasEnPantalla: List<CitaDetalleN> = emptyList()

    fun iniciarPantalla(tokenSesion: String?) { // guarda el token y muestra la pestaña y la semana
        if (tokenSesion != null && tokenSesion.isNotEmpty()) {
            token = tokenSesion
        } else {
            vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
        }
        vista.marcarPestania(pestania)
        mostrarSemana()
    }

    fun cargarCitas() { // pide las citas del alumno y las muestra
        if (token.isEmpty()) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val respuesta = modelo.pedirCitas(token)
                val datos = respuesta.body()
                if (respuesta.isSuccessful && datos != null && datos.status) {
                    val citas = datos.data
                    if (citas != null) {
                        todasLasCitas = citas
                    }
                    mostrarCitas()
                } else if (respuesta.code() == 401) {
                    vista.mostrarError("Tu sesión terminó. Vuelve a iniciar sesión.")
                } else {
                    vista.mostrarError("No pudimos cargar tus citas. Intenta de nuevo.")
                }
            } catch (e: Exception) {
                vista.mostrarError("Revisa tu conexión a internet e intenta de nuevo.")
            }
        }
    }

    fun elegirPestania(pestaniaElegida: String) { // cambia entre proximas e historial
        pestania = pestaniaElegida
        vista.marcarPestania(pestania)
        mostrarCitas()
    }

    fun semanaAnterior() { // regresa una semana
        lunesSemana = modelo.moverDias(lunesSemana, -7)
        mostrarSemana()
    }

    fun semanaSiguiente() { // avanza una semana
        lunesSemana = modelo.moverDias(lunesSemana, 7)
        mostrarSemana()
    }

    private fun mostrarSemana() { // manda a la vista el mes y los 7 dias, y marca el dia elegido
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        vista.mostrarMes(modelo.textoDelMes(lunesSemana))
        vista.mostrarSemana(dias)
        var diaMarcado = -1
        for (i in 0 until dias.size) {
            if (dias[i].fechaServidor == fechaElegida) {
                diaMarcado = i
            }
        }
        vista.marcarDia(diaMarcado)
    }

    fun elegirDia(numeroDia: Int) { // filtra las citas del dia elegido, si lo toca otra vez se quita
        val dias = modelo.armarDiasDeLaSemana(lunesSemana)
        if (dias[numeroDia].fechaServidor == fechaElegida) {
            fechaElegida = ""
            vista.marcarDia(-1)
        } else {
            fechaElegida = dias[numeroDia].fechaServidor
            vista.marcarDia(numeroDia)
        }
        mostrarCitas()
    }

    private fun mostrarCitas() { // arma las tarjetas segun la pestaña y el dia elegido
        val proximas = modelo.citasProximas(todasLasCitas)
        vista.mostrarTextoProximas("Próximas (" + proximas.size + ")")

        var citas = proximas
        if (pestania == "historial") {
            citas = modelo.citasDelHistorial(todasLasCitas)
        }
        if (fechaElegida.isNotEmpty()) {
            citas = modelo.citasDelDia(citas, fechaElegida)
        }
        citasEnPantalla = citas

        if (citas.isEmpty()) {
            vista.mostrarTituloSeccion("No tienes citas aquí")
        } else if (pestania == "historial") {
            vista.mostrarTituloSeccion("Historial de citas")
        } else {
            vista.mostrarTituloSeccion("Citas agendadas")
        }

        val tarjetas = mutableListOf<TarjetaCitaN>()
        for (cita in citas) {
            tarjetas.add(
                TarjetaCitaN(
                    modelo.textoOVacio(cita.motivo),
                    modelo.estadoParaMostrar(cita),
                    modelo.textoFechaTarjeta(cita),
                    modelo.textoDoctora(cita)
                )
            )
        }
        vista.mostrarCitas(tarjetas)
    }

    fun verDetalle(posicion: Int) { // abre el detalle de la cita que toco el alumno
        if (posicion >= 0 && posicion < citasEnPantalla.size) {
            val cita = citasEnPantalla[posicion]
            vista.abrirDetalle(token, cita.idCita)
        }
    }

    fun solicitarCita() { // abre agendar cita
        vista.abrirAgendarCita(token)
    }
}
