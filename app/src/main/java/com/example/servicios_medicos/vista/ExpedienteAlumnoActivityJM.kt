package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsExpedienteAlumnoPresentadorJM
import com.google.android.material.button.MaterialButton

class ExpedienteAlumnoActivityJM : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtTitulo: TextView
    private lateinit var btnAjustes: ImageButton
    private lateinit var txtNombreAlumno: TextView
    private lateinit var txtMatriculaActualizacion: TextView
    private lateinit var txtTipoSangre: TextView
    private lateinit var txtAlergias: TextView
    private lateinit var txtPadecimientos: TextView
    private lateinit var txtCirugias: TextView
    private lateinit var txtTratamiento: TextView
    private lateinit var txtSeccionConsultas: TextView
    private lateinit var btnVerHistorial: MaterialButton
    private lateinit var rvConsultasRecientes: RecyclerView
    private lateinit var btnSolicitarExpediente: MaterialButton
    private lateinit var txtNota: TextView
    private lateinit var presentador: clsExpedienteAlumnoPresentadorJM
    private var token = ""
    private var matricula = ""

    override fun onCreate(savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca el expediente del alumno
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expediente_alumno_jm)

        btnRegresar = findViewById(R.id.btnRegresar)
        txtTitulo = findViewById(R.id.txtTitulo)
        btnAjustes = findViewById(R.id.btnAjustes)
        txtNombreAlumno = findViewById(R.id.txtNombreAlumno)
        txtMatriculaActualizacion = findViewById(R.id.txtMatriculaActualizacion)
        txtTipoSangre = findViewById(R.id.txtTipoSangre)
        txtAlergias = findViewById(R.id.txtAlergias)
        txtPadecimientos = findViewById(R.id.txtPadecimientos)
        txtCirugias = findViewById(R.id.txtCirugias)
        txtTratamiento = findViewById(R.id.txtTratamiento)
        txtSeccionConsultas = findViewById(R.id.txtSeccionConsultas)
        btnVerHistorial = findViewById(R.id.btnVerHistorial)
        rvConsultasRecientes = findViewById(R.id.rvConsultasRecientes)
        btnSolicitarExpediente = findViewById(R.id.btnSolicitarExpediente)
        txtNota = findViewById(R.id.txtNota)

        configurarListeners()

        presentador = clsExpedienteAlumnoPresentadorJM(this)
        val tokenRecibido = intent.getStringExtra("token")
        if (tokenRecibido != null) {
            token = tokenRecibido
        }
        val matriculaRecibida = intent.getStringExtra("matricula")
        if (matriculaRecibida != null) {
            matricula = matriculaRecibida
        }
        presentador.iniciarPantalla(token, matricula)
    }

    private fun configurarListeners() { // define que hace cada boton de la pantalla
        btnRegresar.setOnClickListener {
            finish()
        }
        btnAjustes.setOnClickListener {
            val destino = Intent(this, ConfiguracionActivityC::class.java)
            destino.putExtra("token", token)
            startActivity(destino)
        }
        btnVerHistorial.setOnClickListener {
            val destino = Intent(this, ExpedienteAlumnoActivityJM::class.java)
            destino.putExtra("token", token)
            destino.putExtra("matricula", matricula)
            startActivity(destino)
        }
        btnSolicitarExpediente.setOnClickListener {
            Toast.makeText(this, "Solicitud de expediente enviada.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() { // carga los datos del expediente cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarExpedienteAlumno()
    }

    fun mostrarDatosAlumno(nombre: String, matriculaActualizacion: String) { // muestra nombre del alumno, matricula y fecha de actualizacion
        txtNombreAlumno.text = nombre
        txtMatriculaActualizacion.text = matriculaActualizacion
    }

    fun mostrarDatosClinicosClave(tipoSangre: String, alergias: String) { // muestra tipo de sangre y alergias en las tarjetas superiores
        txtTipoSangre.text = tipoSangre
        txtAlergias.text = alergias
    }

    fun mostrarAntecedentes(padecimientos: String, cirugias: String, tratamiento: String) { // muestra padecimientos, cirugias y tratamiento habitual
        txtPadecimientos.text = padecimientos
        txtCirugias.text = cirugias
        txtTratamiento.text = tratamiento
    }

    fun mostrarConsultasRecientes(consultas: List<Any>) { // guarda las consultas recientes para cuando conectes el adaptador
        rvConsultasRecientes.tag = consultas
    }

    fun mostrarMensajeSolicitud(mensaje: String) { // confirma cuando se envio la solicitud de expediente
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}