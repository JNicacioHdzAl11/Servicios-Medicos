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

class InicioAlumnoFragmentN : Fragment() {
    private lateinit var txtSaludo: TextView
    private lateinit var txtMatricula: TextView
    private lateinit var txtFechaHoy: TextView
    private lateinit var txtCitasProximasValor: TextView
    private lateinit var txtEstadoSalud: TextView
    private lateinit var txtCita1Titulo: TextView
    private lateinit var txtCita1Estado: TextView
    private lateinit var txtCita1Fecha: TextView
    private lateinit var txtCita1Medico: TextView
    private lateinit var txtVerTodasCitas: TextView
    private lateinit var btnSolicitarCita: MaterialButton
    private lateinit var btnRegresar: ImageButton
    private lateinit var btnAjustes: ImageButton
    private lateinit var txtCampania1Titulo: TextView
    private lateinit var txtCampania1Detalle: TextView
    private lateinit var txtCampania2Titulo: TextView
    private lateinit var txtCampania2Detalle: TextView
    private lateinit var presentador: clsInicioAlumnoPresentadorN
    private var token = ""
    private var regresoDeAgendar = false

    override fun onCreateView( // carga el diseño XML del fragment
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio_alumno_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // enlaza el XML, crea el presentador y arranca la pantalla
        super.onViewCreated(view, savedInstanceState)

        txtSaludo = view.findViewById(R.id.txtSaludo)
        txtMatricula = view.findViewById(R.id.txtMatricula)
        txtFechaHoy = view.findViewById(R.id.txtFechaHoy)
        txtCitasProximasValor = view.findViewById(R.id.txtCitasProximasValor)
        txtEstadoSalud = view.findViewById(R.id.txtEstadoSaludValor)
        txtCita1Titulo = view.findViewById(R.id.txtCita1Titulo)
        txtCita1Estado = view.findViewById(R.id.txtCita1Estado)
        txtCita1Fecha = view.findViewById(R.id.txtCita1Fecha)
        txtCita1Medico = view.findViewById(R.id.txtCita1Medico)
        txtVerTodasCitas = view.findViewById(R.id.txtVerTodasCitas)
        btnSolicitarCita = view.findViewById(R.id.btnSolicitarCita)
        btnRegresar = view.findViewById(R.id.ibRegresar)
        btnAjustes = view.findViewById(R.id.ibAjustes)
        btnAjustes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val destino = Intent(requireContext(), ConfiguracionActivityC::class.java)
                destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
                destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
                startActivity(destino)
            }
        })
        // los id del XML se quedan igual, solo cambia el nombre de la variable
        txtCampania1Titulo = view.findViewById(R.id.txtAvisoVacunacionTitulo)
        txtCampania1Detalle = view.findViewById(R.id.txtAvisoVacunacion)
        txtCampania2Titulo = view.findViewById(R.id.txtAvisoBienestarTitulo)
        txtCampania2Detalle = view.findViewById(R.id.txtAvisoBienestar)

        btnRegresar.setOnClickListener { // regresa al login
            val intent = Intent(requireContext(), LoginActivityC::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        btnSolicitarCita.setOnClickListener { // abre agendar cita
            val intent = Intent(requireContext(), AgendarCitaActivityN::class.java)
            intent.putExtra("token", token)
            regresoDeAgendar = true
            startActivity(intent)
        }
        txtVerTodasCitas.setOnClickListener { // abre mis citas desde el menu de abajo
            val menu = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
            menu.selectedItemId = R.id.nav_citas
        }
        presentador = clsInicioAlumnoPresentadorN(this)
        val intent = requireActivity().intent
        val tokenRecibido = intent.getStringExtra("token")
        if (tokenRecibido != null) {
            token = tokenRecibido
        }
        val nombre = intent.getStringExtra("nombre")
        var matricula = intent.getStringExtra("matricula")
        if (matricula == null) {
            matricula = intent.getStringExtra("identificador")
        }
        presentador.iniciarPantalla(token, nombre, matricula)
    }
    override fun onResume() { // al regresar de agendar cita se recargan los datos
        super.onResume()
        if (regresoDeAgendar && token.isNotEmpty()) {
            regresoDeAgendar = false
            presentador.cargarDatosInicio(token)
        }
    }
    fun mostrarSaludo(texto: String) { //muestra el saludo
        txtSaludo.text = texto
    }
    fun mostrarMatricula(texto: String) {// muestra la matricula del alumno
        txtMatricula.text = texto
    }
    fun mostrarFechaHoy(fecha: String) { // muestra la fecha de hoy
        txtFechaHoy.text = fecha
    }
    fun mostrarResumen(citasProximas: String, estadoSalud: String) { // muestra cuantas citas proximas hay
        txtCitasProximasValor.text = citasProximas
        txtEstadoSalud.text = estadoSalud
    }

    fun mostrarProximaCita(titulo: String, estado: String, fecha: String, medico: String) { // muestra la proxima cita
        txtCita1Titulo.text = titulo
        txtCita1Estado.text = estado
        txtCita1Fecha.text = fecha
        txtCita1Medico.text = medico
    }
    fun mostrarCampania1(titulo: String, detalle: String) { // muestra la 1ra campaña
        txtCampania1Titulo.text = titulo
        txtCampania1Detalle.text = detalle
    }
    fun mostrarCampania2(titulo: String, detalle: String) { // muestra la 2da campaña
        txtCampania2Titulo.text = titulo
        txtCampania2Detalle.text = detalle
    }
    fun mostrarError(mensaje: String) {// muestra el mensaje de error
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}
