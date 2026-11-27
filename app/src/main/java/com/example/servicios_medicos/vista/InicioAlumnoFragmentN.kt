package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsInicioAlumnoPresentadorN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import  androidx.activity.OnBackPressedCallback

class InicioAlumnoFragmentN : Fragment() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtSaludo: TextView
    private lateinit var txtMatricula: TextView
    private lateinit var txtFechaHoy: TextView
    private lateinit var txtCitasProximasValor: TextView
    private lateinit var txtEstadoSalud: TextView
    private lateinit var txtEstadoDetalle: TextView
    private lateinit var txtCita1Titulo: TextView
    private lateinit var txtCita1Estado: TextView
    private lateinit var txtCita1Fecha: TextView
    private lateinit var txtCita1Medico: TextView
    private lateinit var txtVerTodasCitas: TextView
    private lateinit var btnSolicitarCita: MaterialButton
    private lateinit var btnAjustes: ImageButton
    private lateinit var cardAvisos: MaterialCardView
    private lateinit var txtCampania1Titulo: TextView
    private lateinit var txtCampania1Detalle: TextView
    private lateinit var txtCampania2Titulo: TextView
    private lateinit var txtCampania2Detalle: TextView
    private lateinit var presentador: clsInicioAlumnoPresentadorN

    override fun onCreateView( // carga el diseño de la pantalla de inicio
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio_alumno_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca el inicio del alumno
        super.onViewCreated(view, savedInstanceState)


        btnRegresar = view.findViewById(R.id.ibRegresar)
        txtSaludo = view.findViewById(R.id.txtSaludo)
        txtMatricula = view.findViewById(R.id.txtMatricula)
        txtFechaHoy = view.findViewById(R.id.txtFechaHoy)
        txtCitasProximasValor = view.findViewById(R.id.txtCitasProximasValor)
        txtEstadoSalud = view.findViewById(R.id.txtEstadoSaludValor)
        txtEstadoDetalle = view.findViewById(R.id.tvEstadoSaludEtiqueta)
        txtCita1Titulo = view.findViewById(R.id.txtCita1Titulo)
        txtCita1Estado = view.findViewById(R.id.txtCita1Estado)
        txtCita1Fecha = view.findViewById(R.id.txtCita1Fecha)
        txtCita1Medico = view.findViewById(R.id.txtCita1Medico)
        txtVerTodasCitas = view.findViewById(R.id.txtVerTodasCitas)
        btnSolicitarCita = view.findViewById(R.id.btnSolicitarCita)
        btnAjustes = view.findViewById(R.id.ibAjustes)
        cardAvisos = view.findViewById(R.id.cvAvisos)
        txtCampania1Titulo = view.findViewById(R.id.txtAvisoVacunacionTitulo)
        txtCampania1Detalle = view.findViewById(R.id.txtAvisoVacunacion)
        txtCampania2Titulo = view.findViewById(R.id.txtAvisoBienestarTitulo)
        txtCampania2Detalle = view.findViewById(R.id.txtAvisoBienestar)

        configurarListeners()
        configurarBotonAtras()

        presentador = clsInicioAlumnoPresentadorN(this)
        presentador.iniciarPantalla(requireActivity().intent.getStringExtra("token"))
    }
    private fun configurarListeners() { // dice que hace solicitar cita, ver todas y avisos de salud
        btnSolicitarCita.setOnClickListener {
            presentador.solicitarCita()
        }
        txtVerTodasCitas.setOnClickListener {
            presentador.verTodasLasCitas()
        }
        cardAvisos.setOnClickListener {
            presentador.verCampanias()
        }
        btnRegresar.setOnClickListener {
            presentador.regresarAlLogin()
        }
        btnAjustes.setOnClickListener {
            presentador.verConfiguracion()
        }

    }
    private fun configurarBotonAtras() { // en inicio el boton atras del telefono no hace nada, solo se sale con la flecha
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { // se ejecuta al picar atras o hacer el gesto
            }
        })
    }
    override fun onResume() { // carga los datos cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarDatosInicio()
    }
    fun mostrarAlumno(saludo: String, matricula: String) { // muestra el saludo y la matricula del alumno
        txtSaludo.text = saludo
        txtMatricula.text = matricula
    }
    fun mostrarFechaHoy(fecha: String) { // muestra la fecha de hoy
        txtFechaHoy.text = fecha
    }
    fun mostrarResumen(citasProximas: String, estadoSalud: String, pendientes: String) { // muestra cuantas citas proximas hay, el estado del alumno y sus pendientes
        txtCitasProximasValor.text = citasProximas
        txtEstadoSalud.text = estadoSalud
        txtEstadoDetalle.text = pendientes
    }

    fun mostrarProximaCita(titulo: String, estado: String, fecha: String, horario: String) { // muestra la tarjeta de tu proxima cita
        txtCita1Titulo.text = titulo
        txtCita1Estado.text = estado
        txtCita1Fecha.text = fecha
        txtCita1Medico.text = horario
        if (estado == "Pendiente") {
            txtCita1Estado.isSelected = true
        } else {
            txtCita1Estado.isSelected = false
        }
        if (estado == "Cancelada") {
            txtCita1Estado.isActivated = true
        } else {
            txtCita1Estado.isActivated = false
        }
    }
    fun mostrarCampania(numero: Int, titulo: String, detalle: String) { // muestra la campaña 1 o 2 de salud
        if (numero == 1) {
            txtCampania1Titulo.text = titulo
            txtCampania1Detalle.text = detalle
        } else {
            txtCampania2Titulo.text = titulo
            txtCampania2Detalle.text = detalle
        }
    }
    fun abrirAgendarCita(token: String) { // abre agendar cita
        val intent = Intent(requireContext(), AgendarCitaActivityN::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }
    fun abrirMisCitas() { // cambia el menu de abajo a mis citas
        val menu = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
        menu.selectedItemId = R.id.nav_citas
    }
    fun abrirCampanias() { // cambia el menu de abajo a campañas
        val menu = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
        menu.selectedItemId = R.id.nav_campanias
    }
    fun mostrarMensaje(mensaje: String) { // muestra un aviso al alumno
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
    fun abrirLogin() { // regresa al login y cierra la pantalla principal
        val intent = Intent(requireContext(), LoginActivityC::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    fun abrirConfiguracion() { // abre la pantalla de configuracion
        val intent = Intent(requireContext(), ConfiguracionActivityC::class.java)
        intent.putExtra("token", requireActivity().intent.getStringExtra("token"))
        intent.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
        startActivity(intent)
    }
}
