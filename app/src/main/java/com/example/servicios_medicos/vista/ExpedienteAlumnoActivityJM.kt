package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsExpedienteAlumnoPresentadorJM
import com.google.android.material.button.MaterialButton

class ExpedienteAlumnoActivityJM : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var btnAjustesExpediente: ImageButton
    private lateinit var txtIniciales: TextView
    private lateinit var txtNombrePaciente: TextView
    private lateinit var txtMatriculaEdad: TextView
    private lateinit var txtCarreraGrupo: TextView
    private lateinit var txtAlertaAlergia: TextView
    private lateinit var txtTipoSangre: TextView
    private lateinit var txtEnfermedadesCronicas: TextView
    private lateinit var txtAlergenosDetalle: TextView
    private lateinit var txtCirugiasPrevias: TextView
    private lateinit var txtTratamientoHabitual: TextView
    private lateinit var txtConsulta1: TextView
    private lateinit var txtConsulta2: TextView
    private lateinit var btnVolverInicioDoctora: MaterialButton
    private lateinit var presentador: clsExpedienteAlumnoPresentadorJM
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) { // carga el diseño del expediente del alumno y arranca la pantalla
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expediente_alum_jm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegresar = findViewById(R.id.btnRegresar)
        btnAjustesExpediente = findViewById(R.id.btnAjustesExpediente)
        txtIniciales = findViewById(R.id.txtIniciales)
        txtNombrePaciente = findViewById(R.id.txtNombrePaciente)
        txtMatriculaEdad = findViewById(R.id.txtMatriculaEdad)
        txtCarreraGrupo = findViewById(R.id.txtCarreraGrupo)
        txtAlertaAlergia = findViewById(R.id.txtAlertaAlergia)
        txtTipoSangre = findViewById(R.id.txtTipoSangre)
        txtEnfermedadesCronicas = findViewById(R.id.txtEnfermedadesCronicas)
        txtAlergenosDetalle = findViewById(R.id.txtAlergenosDetalle)
        txtCirugiasPrevias = findViewById(R.id.txtCirugiasPrevias)
        txtTratamientoHabitual = findViewById(R.id.txtTratamientoHabitual)
        txtConsulta1 = findViewById(R.id.txtConsulta1)
        txtConsulta2 = findViewById(R.id.txtConsulta2)
        btnVolverInicioDoctora = findViewById(R.id.btnVolverInicioDoctora)

        configurarListeners()

        presentador = clsExpedienteAlumnoPresentadorJM(this)
        val tokenRecibido = intent.getStringExtra("token")
        if (tokenRecibido != null) {
            token = tokenRecibido
        }
        presentador.iniciarPantalla(token, intent.getStringExtra("matricula"))
    }

    private fun configurarListeners() { // conecta regreso y configuracion (sin abrir consulta ni receta)
        btnRegresar.setOnClickListener {
            finish()
        }
        btnVolverInicioDoctora.setOnClickListener {
            finish()
        }
        btnAjustesExpediente.setOnClickListener {
            val destino = Intent(this, ConfiguracionDoctoraActivityJM::class.java)
            destino.putExtra("token", token)
            startActivity(destino)
        }
    }

    override fun onResume() { // carga los datos cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarExpediente()
    }

    fun mostrarPaciente(iniciales: String, nombre: String, matriculaEdad: String, carreraGrupo: String) { // muestra datos generales del alumno
        txtIniciales.text = iniciales
        txtNombrePaciente.text = nombre
        txtMatriculaEdad.text = matriculaEdad
        txtCarreraGrupo.text = carreraGrupo
    }

    fun mostrarClinicos(alergia: String, tipoSangre: String, enfermedades: String) { // muestra alergia, sangre y enfermedades cronicas
        txtAlertaAlergia.text = alergia
        txtTipoSangre.text = tipoSangre
        txtEnfermedadesCronicas.text = enfermedades
    }

    fun mostrarAntecedentes(alergenos: String, cirugias: String, tratamiento: String) { // muestra antecedentes del alumno
        txtAlergenosDetalle.text = alergenos
        txtCirugiasPrevias.text = cirugias
        txtTratamientoHabitual.text = tratamiento
    }

    fun mostrarConsultas(consulta1: String, consulta2: String) { // muestra las consultas recientes
        txtConsulta1.text = consulta1
        txtConsulta2.text = consulta2
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}