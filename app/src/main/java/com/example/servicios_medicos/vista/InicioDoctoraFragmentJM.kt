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
import com.google.android.material.button.MaterialButton
import com.example.servicios_medicos.presentador.clsInicioDoctoraPresentadorJM

class InicioDoctoraFragmentJM : Fragment() {
    private lateinit var txtSaludoDoctora: TextView
    private lateinit var txtCedulaDoctora: TextView
    private lateinit var txtFechaActual: TextView
    private lateinit var txtTotalCitasHoy: TextView
    private lateinit var txtTotalAtenciones: TextView
    private lateinit var txtInicialesPaciente: TextView
    private lateinit var txtNombrePaciente: TextView
    private lateinit var txtMatriculaEdad: TextView
    private lateinit var txtMotivoConsulta: TextView
    private lateinit var txtSignosVitalesResumen: TextView
    private lateinit var txtProxima1: TextView
    private lateinit var txtProxima2: TextView
    private lateinit var btnAbrirExpediente: MaterialButton
    private lateinit var btnAjustesDoctora: ImageButton
    private lateinit var txtAbrirConfiguracion: TextView
    private lateinit var presentador: clsInicioDoctoraPresentadorJM
    private var token = ""
    private var matriculaEspera = ""

    override fun onCreateView( // carga el diseño de la pantalla de inicio de la doctora
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio_doctora_jm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca el inicio de la doctora
        super.onViewCreated(view, savedInstanceState)

        txtSaludoDoctora = view.findViewById(R.id.txtSaludoDoctora)
        txtCedulaDoctora = view.findViewById(R.id.txtCedulaDoctora)
        txtFechaActual = view.findViewById(R.id.txtFechaActual)
        txtTotalCitasHoy = view.findViewById(R.id.txtTotalCitasHoy)
        txtTotalAtenciones = view.findViewById(R.id.txtTotalAtenciones)
        txtInicialesPaciente = view.findViewById(R.id.txtInicialesPaciente)
        txtNombrePaciente = view.findViewById(R.id.txtNombrePaciente)
        txtMatriculaEdad = view.findViewById(R.id.txtMatriculaEdad)
        txtMotivoConsulta = view.findViewById(R.id.txtMotivoConsulta)
        txtSignosVitalesResumen = view.findViewById(R.id.txtSignosVitalesResumen)
        txtProxima1 = view.findViewById(R.id.txtProxima1)
        txtProxima2 = view.findViewById(R.id.txtProxima2)
        btnAbrirExpediente = view.findViewById(R.id.btnVerExpedienteAlumno)
        btnAjustesDoctora = view.findViewById(R.id.btnConfiguracion)
        txtAbrirConfiguracion = view.findViewById(R.id.txtAbrirConfiguracion)

        configurarListeners()

        presentador = clsInicioDoctoraPresentadorJM(this)
        val tokenRecibido = requireActivity().intent.getStringExtra("token")
        if (tokenRecibido != null) {
            token = tokenRecibido
        }
        presentador.iniciarPantalla(token)
    }

    private fun configurarListeners() { // dice que hace cada boton de la pantalla
        btnAjustesDoctora.setOnClickListener {
            val destino = Intent(requireContext(), ConfiguracionDoctoraActivityJM::class.java)
            destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
            destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
            startActivity(destino)
        }
        txtAbrirConfiguracion.setOnClickListener {
            btnAjustesDoctora.performClick()
        }
        btnAbrirExpediente.setOnClickListener {
            val destino = Intent(requireContext(), ExpedienteMedicoAlumActivityJM::class.java)
            destino.putExtra("token", token)
            destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
            destino.putExtra("matricula", matriculaEspera)
            startActivity(destino)
        }
    }

    override fun onResume() { // carga los datos cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarInicioDoctora()
    }

    fun mostrarFechaHoy(fecha: String) { // muestra la fecha de hoy
        txtFechaActual.text = fecha
    }

    fun mostrarEncabezadoDoctora(saludo: String, cedula: String, fecha: String) { // muestra datos de la doctora y fecha
        txtSaludoDoctora.text = saludo
        txtCedulaDoctora.text = cedula
        txtFechaActual.text = fecha
    }

    fun mostrarContadores(citasHoy: String, atenciones: String) { // muestra citas de hoy y consultas realizadas
        txtTotalCitasHoy.text = citasHoy
        txtTotalAtenciones.text = atenciones
    }

    fun mostrarPacienteEnEspera(
        matricula: String,
        iniciales: String,
        nombre: String,
        detalle: String,
        motivo: String,
        signos: String
    ) { // muestra los datos del paciente en espera
        if (matricula.isNotEmpty()) {
            matriculaEspera = matricula
        }
        txtInicialesPaciente.text = iniciales
        txtNombrePaciente.text = nombre
        txtMatriculaEdad.text = detalle
        txtMotivoConsulta.text = motivo
        txtSignosVitalesResumen.text = signos
    }

    fun mostrarProximasConsultas(prox1: String, prox2: String) { // muestra las proximas consultas
        txtProxima1.text = prox1
        txtProxima2.text = prox2
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}