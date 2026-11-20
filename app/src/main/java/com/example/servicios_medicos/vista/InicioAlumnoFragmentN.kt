package com.example.servicios_medicos.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.CampaniaModeloN
import com.example.servicios_medicos.modelo.CitaModeloN
import com.example.servicios_medicos.presentador.clsInicioAlumnoPresentadorN
import com.google.android.material.button.MaterialButton
import android.content.Intent
import com.example.servicios_medicos.modelo.AlumnoInicioModeloN

class InicioAlumnoFragmentN : Fragment() {

    private lateinit var tvSaludo: TextView
    private lateinit var tvMatricula: TextView
    private lateinit var tvFechaHoy: TextView
    private lateinit var tvCitasProximasValor: TextView
    private lateinit var tvEstadoSaludValor: TextView
    private lateinit var tvCita1Titulo: TextView
    private lateinit var tvCita1Estado: TextView
    private lateinit var tvCita1Fecha: TextView
    private lateinit var tvCita1Medico: TextView
    private lateinit var tvVerTodasCitas: TextView
    private lateinit var btnSolicitarCita: MaterialButton
    private lateinit var ibRegresar: ImageButton
    private lateinit var ibAjustes: ImageButton
    private lateinit var presentador: clsInicioAlumnoPresentadorN
    private lateinit var tvAvisoVacunacionTitulo: TextView
    private lateinit var tvAvisoVacunacion: TextView
    private lateinit var tvAvisoBienestarTitulo: TextView
    private lateinit var tvAvisoBienestar: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio_alumno_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvSaludo = view.findViewById(R.id.tvSaludo)
        tvMatricula = view.findViewById(R.id.tvMatricula)
        tvFechaHoy = view.findViewById(R.id.tvFechaHoy)
        tvCitasProximasValor = view.findViewById(R.id.tvCitasProximasValor)
        tvEstadoSaludValor = view.findViewById(R.id.tvEstadoSaludValor)
        tvCita1Titulo = view.findViewById(R.id.tvCita1Titulo)
        tvCita1Estado = view.findViewById(R.id.tvCita1Estado)
        tvCita1Fecha = view.findViewById(R.id.tvCita1Fecha)
        tvCita1Medico = view.findViewById(R.id.tvCita1Medico)
        tvVerTodasCitas = view.findViewById(R.id.tvVerTodasCitas)
        btnSolicitarCita = view.findViewById(R.id.btnSolicitarCita)
        ibRegresar = view.findViewById(R.id.ibRegresar)
        ibAjustes = view.findViewById(R.id.ibAjustes)

        tvAvisoVacunacionTitulo = view.findViewById(R.id.tvAvisoVacunacionTitulo)
        tvAvisoVacunacion = view.findViewById(R.id.tvAvisoVacunacion)
        tvAvisoBienestarTitulo = view.findViewById(R.id.tvAvisoBienestarTitulo)
        tvAvisoBienestar = view.findViewById(R.id.tvAvisoBienestar)

        presentador = clsInicioAlumnoPresentadorN(this)

        configurarListeners()

        val token = requireActivity().intent.getStringExtra("token") ?: ""
        val matriculaLogin = requireActivity().intent.getStringExtra("identificador") ?: ""
        if (matriculaLogin.isNotEmpty()) {
            tvMatricula.text = "Matrícula $matriculaLogin · Alumno"
        }

        if (token.isNotEmpty()) {
            presentador.cargarDatosInicio(token)
        } else {
            mostrarError("No se recibió token de sesión")
        }
        val formatoFecha = java.text.SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", java.util.Locale.forLanguageTag("es-MX"))
        val fechaActual = formatoFecha.format(java.util.Date())
        tvFechaHoy.text = fechaActual.replaceFirstChar { it.uppercase() }
    }

    private fun configurarListeners() {
        btnSolicitarCita.setOnClickListener {
            val intent = Intent(requireContext(), AgendarCitaActivityN::class.java)
            startActivity(intent)
        }

        tvVerTodasCitas.setOnClickListener {
        }

        ibAjustes.setOnClickListener {
            val intent = Intent(requireContext(), ConfiguracionActivityC::class.java)
            startActivity(intent)
        }

        ibRegresar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    fun mostrarCargando(cargando: Boolean) {
        btnSolicitarCita.isEnabled = !cargando
    }

    fun mostrarDatosAlumno(alumno: AlumnoInicioModeloN?) {
        if (alumno != null) {
            val primerNombre = alumno.nombreCompleto.trim().split(" ").firstOrNull() ?: alumno.nombreCompleto
            if (primerNombre.isNotEmpty()) {
                tvSaludo.text = "Hola, $primerNombre"
            }
            if (alumno.matricula.isNotEmpty()) {
                tvMatricula.text = "Matrícula ${alumno.matricula} · Alumno"
            }
        }
    }

    fun mostrarProximaCita(cita: CitaModeloN?) {
        if (cita != null) {
            tvCitasProximasValor.text = "01"
            tvEstadoSaludValor.text = cita.estado
            tvCita1Titulo.text = cita.motivo
            tvCita1Estado.text = cita.estado
            tvCita1Fecha.text = "${cita.fecha} · ${cita.hora}"
            tvCita1Medico.text = "Servicios Médicos UTHH"
        } else {
            tvCitasProximasValor.text = "00"
            tvEstadoSaludValor.text = "Al día"
            tvCita1Titulo.text = "Sin citas programadas"
            tvCita1Estado.text = ""
            tvCita1Fecha.text = "Agenda una consulta cuando lo necesites"
            tvCita1Medico.text = ""
        }
    }
    fun mostrarCampanias(campanias: List<CampaniaModeloN>) {
        if (campanias.isNotEmpty()) {
            tvAvisoVacunacionTitulo.text = campanias[0].titulo
            tvAvisoVacunacion.text = "${campanias[0].fechaInicio} · ${campanias[0].lugar}"
        } else {
            tvAvisoVacunacionTitulo.text = "Sin avisos de salud"
            tvAvisoVacunacion.text = ""
        }

        if (campanias.size > 1) {
            tvAvisoBienestarTitulo.text = campanias[1].titulo
            tvAvisoBienestar.text = "${campanias[1].fechaInicio} · ${campanias[1].lugar}"
        } else {
            tvAvisoBienestarTitulo.text = ""
            tvAvisoBienestar.text = ""
        }
    }
    fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}