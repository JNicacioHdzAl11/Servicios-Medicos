package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class clsAgendarCitaModeloN {

    fun listaDeMotivos(): List<String> { // regresa los motivos que puede elegir el alumno
        return listOf("Consulta general", "Nutrición", "Psicología", "Otro motivo")
    }

    fun fechaDeHoy(): String { // regresa la fecha de hoy como la usa el servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(Date())
    }

    fun horaDeAhorita(): String { // regresa la hora de ahorita
        val formato = SimpleDateFormat("HH:mm", Locale.US)
        return formato.format(Date())
    }

    fun buscarLunesDeLaSemana(fecha: Calendar): Calendar { // va hacia atras hasta encontrar el lunes
        val lunes = Calendar.getInstance()
        lunes.time = fecha.time
        while (lunes.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            lunes.add(Calendar.DAY_OF_MONTH, -1)
        }
        return lunes
    }

    fun moverSemanas(lunes: Calendar, semanas: Int): Calendar { // suma las semanas que se le pidan al lunes
        val nuevoLunes = Calendar.getInstance()
        nuevoLunes.time = lunes.time
        nuevoLunes.add(Calendar.DAY_OF_MONTH, 7 * semanas)
        return nuevoLunes
    }

    fun fechaDelDia(lunes: Calendar, numeroDia: Int): Calendar { // regresa la fecha de un dia de la semana
        val fecha = Calendar.getInstance()
        fecha.time = lunes.time
        fecha.add(Calendar.DAY_OF_MONTH, numeroDia)
        return fecha
    }

    fun fechaParaServidor(fecha: Calendar): String { // cambia la fecha como la pide el servidor
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formato.format(fecha.time)
    }

    fun armarDiasDeLaSemana(lunes: Calendar): List<DiaSemanaN> { // arma los 7 dias, solo se puede de lunes a viernes y desde hoy
        val letras = listOf("L", "M", "M", "J", "V", "S", "D")
        val hoy = fechaDeHoy()
        val dias = mutableListOf<DiaSemanaN>()
        for (i in 0 until 7) {
            val fecha = fechaDelDia(lunes, i)
            val numero = fecha.get(Calendar.DAY_OF_MONTH).toString()
            val textoFecha = fechaParaServidor(fecha)
            var sePuede = true
            if (textoFecha < hoy || i >= 5) {
                sePuede = false
            }
            dias.add(DiaSemanaN(letras[i], numero, textoFecha, sePuede))
        }
        return dias
    }

    fun textoDelMes(fecha: Calendar): String { // arma el nombre del mes y el año
        val formato = SimpleDateFormat("MMMM yyyy", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return ponerMayuscula(texto)
    }

    fun textoDiaElegido(fecha: Calendar): String { // arma el texto del dia que eligio el alumno
        val formato = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "MX"))
        val texto = formato.format(fecha.time)
        return ponerMayuscula(texto) + " · Horarios disponibles"
    }

    fun textoFechaCorta(fecha: Calendar): String { // arma la fecha corta igual que en mis citas
        val dias = listOf("Dom.", "Lun.", "Mar.", "Mié.", "Jue.", "Vie.", "Sáb.")
        val meses = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
        val dia = dias[fecha.get(Calendar.DAY_OF_WEEK) - 1]
        val numero = fecha.get(Calendar.DAY_OF_MONTH)
        val mes = meses[fecha.get(Calendar.MONTH)]
        return dia + " " + numero + " " + mes
    }

    private fun ponerMayuscula(texto: String): String { // pone en mayuscula la primera letra
        if (texto.isEmpty()) {
            return texto
        }
        return texto.substring(0, 1).uppercase() + texto.substring(1)
    }

    suspend fun tieneCitaEseDia(token: String, fecha: String): Boolean { // revisa si el alumno ya tiene una cita pendiente ese dia
        val respuesta = RetrofitCliente.apiAlumnoN.listarCitas("Bearer $token")
        val datos = respuesta.body()
        if (respuesta.isSuccessful && datos != null) {
            val citas = datos.data
            if (citas != null) {
                for (cita in citas) {
                    if (cita.fecha == fecha && cita.estado == "Pendiente") {
                        return true
                    }
                }
            }
        }
        return false
    }

    suspend fun pedirHorarios(token: String, fecha: String): Response<HorariosDisponiblesResponseN> { // llama a la api horarios disponibles
        return RetrofitCliente.apiAlumnoN.obtenerHorariosDisponibles("Bearer $token", fecha)
    }

    fun sacarHorasLibres(datos: HorariosDisponiblesResponseN, fecha: String): List<String> { // saca las horas libres sin las que ya pasaron hoy
        val horasLibres = mutableListOf<String>()
        val hoy = fechaDeHoy()
        val ahorita = horaDeAhorita()
        val contenido = datos.data
        if (contenido != null) {
            for (hora in contenido.horarios) {
                if (hora.length >= 5) {
                    val horaCorta = hora.substring(0, 5)
                    if (fecha != hoy || horaCorta > ahorita) {
                        horasLibres.add(horaCorta)
                    }
                }
            }
        }
        return horasLibres
    }

    fun textoDoctora(datos: HorariosDisponiblesResponseN): String { // arma el nombre de la doctora y su consultorio
        val contenido = datos.data
        if (contenido != null) {
            val doctora = contenido.doctora
            val consultorio = contenido.consultorio
            if (doctora != null && doctora.isNotEmpty()) {
                if (consultorio != null && consultorio.isNotEmpty()) {
                    return doctora + " · " + consultorio
                }
                return doctora
            }
        }
        return "Médico por asignar · Servicio Médico"
    }

    fun sumarMediaHora(hora: String): String { // suma 30 minutos a la hora de la cita
        if (hora.length < 5) {
            return hora
        }
        var horas = hora.substring(0, 2).toInt()
        var minutos = hora.substring(3, 5).toInt() + 30
        if (minutos >= 60) {
            minutos = minutos - 60
            horas = horas + 1
        }
        var textoHoras = horas.toString()
        if (horas < 10) {
            textoHoras = "0" + horas
        }
        var textoMinutos = minutos.toString()
        if (minutos < 10) {
            textoMinutos = "0" + minutos
        }
        return textoHoras + ":" + textoMinutos
    }

    fun textoResumenMotivo(motivo: String, textoFecha: String): String { // arma el motivo y la fecha del resumen
        var texto = motivo
        if (motivo.isEmpty()) {
            texto = "Selecciona un motivo"
        }
        if (textoFecha.isEmpty()) {
            return texto + " · Selecciona una fecha"
        }
        return texto + " · " + textoFecha
    }

    fun textoResumenHorario(hora: String): String { // arma la hora de inicio y fin del resumen
        if (hora.isEmpty()) {
            return "Selecciona un horario"
        }
        return hora + "–" + sumarMediaHora(hora) + " · Servicio Médico"
    }

    suspend fun guardarCita(token: String, fecha: String, hora: String, motivo: String): Response<AgendarCitaResponseN> { // llama a la api agendar cita
        val solicitud = AgendarCitaRequestN(fecha, hora, motivo)
        return RetrofitCliente.apiAlumnoN.agendarCita("Bearer $token", solicitud)
    }
}
