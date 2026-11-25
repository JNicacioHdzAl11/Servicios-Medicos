package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsExpedienteAlumPresentadorJM
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExpedienteAlumActivityJM : AppCompatActivity() {
    private lateinit var btnVolver: ImageView
    private lateinit var tvTitulo: TextView
    private lateinit var ivMiniAvatar: ImageView
    private lateinit var tvIniciales: TextView
    private lateinit var tvNombrePaciente: TextView
    private lateinit var tvMatriculaEdad: TextView
    private lateinit var tvCarreraGrupo: TextView
    private lateinit var tvAlertaAlergia: TextView
    private lateinit var tvTipoSangreMedico: TextView
    private lateinit var tvEnfermedadesCronicas: TextView
    private lateinit var tvAlergenosDetalle: TextView
    private lateinit var tvCirugiasPrevias: TextView
    private lateinit var tvTratamientoHabitualMedico: TextView
    private lateinit var tvVerRecetaAnterior1: TextView
    private lateinit var tvVerRecetaAnterior2: TextView
    private lateinit var btnIniciarNuevaConsulta: Button
    private lateinit var bnvNavegacion: BottomNavigationView
    private lateinit var presentador: clsExpedienteAlumPresentadorJM
    private var token = ""
    private var matricula = ""

    override fun onCreate(savedInstanceState: Bundle?) { // carga el diseño de la pantalla y arranca el expediente del alumno
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expediente_alum_jm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver = findViewById(R.id.btnVolver)
        tvTitulo = findViewById(R.id.tvTitulo)
        ivMiniAvatar = findViewById(R.id.ivMiniAvatar)
        tvIniciales = findViewById(R.id.tvIniciales)
        tvNombrePaciente = findViewById(R.id.tvNombrePaciente)
        tvMatriculaEdad = findViewById(R.id.tvMatriculaEdad)
        tvCarreraGrupo = findViewById(R.id.tvCarreraGrupo)
        tvAlertaAlergia = findViewById(R.id.tvAlertaAlergia)
        tvTipoSangreMedico = findViewById(R.id.tvTipoSangreMedico)
        tvEnfermedadesCronicas = findViewById(R.id.tvEnfermedadesCronicas)
        tvAlergenosDetalle = findViewById(R.id.tvAlergenosDetalle)
        tvCirugiasPrevias = findViewById(R.id.tvCirugiasPrevias)
        tvTratamientoHabitualMedico = findViewById(R.id.tvTratamientoHabitualMedico)
        tvVerRecetaAnterior1 = findViewById(R.id.tvVerRecetaAnterior1)
        tvVerRecetaAnterior2 = findViewById(R.id.tvVerRecetaAnterior2)
        btnIniciarNuevaConsulta = findViewById(R.id.btnIniciarNuevaConsulta)
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        configurarListeners()

        presentador = clsExpedienteAlumPresentadorJM(vista = this)
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

    private fun configurarListeners() { // dice que hace cada boton y accion en la pantalla
        btnVolver.setOnClickListener {
            finish()
        }
        tvVerRecetaAnterior1.setOnClickListener {
            presentador.consultarRecetaAnterior(0)
        }
        tvVerRecetaAnterior2.setOnClickListener {
            presentador.consultarRecetaAnterior(1)
        }
        btnIniciarNuevaConsulta.setOnClickListener {
            val destino = Intent(this, ConsultaMedicaActivityJM::class.java)
            destino.putExtra("token", token)
            destino.putExtra("matricula", matricula)
            destino.putExtra("nombre_paciente", tvNombrePaciente.text.toString())
            destino.putExtra("alergia", tvAlertaAlergia.text.toString().removePrefix("Alergia: ").trim())
            startActivity(destino)
        }
        bnvNavegacion.setOnItemSelectedListener { item ->
            Toast.makeText(this, "Sección ${item.title}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onResume() { // carga los datos cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarExpedienteAlumno()
    }

    fun mostrarIniciales(texto: String) { // muestra las iniciales del alumno
        tvIniciales.text = texto
    }

    fun mostrarNombrePaciente(texto: String) { // muestra el nombre del alumno
        tvNombrePaciente.text = texto
    }

    fun mostrarMatriculaEdad(texto: String) { // muestra la matricula y edad del alumno
        tvMatriculaEdad.text = texto
    }

    fun mostrarCarreraGrupo(texto: String) { // muestra carrera, cuatrimestre y grupo
        tvCarreraGrupo.text = texto
    }

    fun mostrarAlertaAlergia(texto: String) { // muestra la alerta de alergia
        tvAlertaAlergia.text = texto
    }

    fun mostrarResumenClinico(tipoSangre: String, enfermedadesCronicas: String) { // muestra tipo de sangre y enfermedades cronicas
        tvTipoSangreMedico.text = tipoSangre
        tvEnfermedadesCronicas.text = enfermedadesCronicas
    }

    fun mostrarAntecedentes(alergenos: String, cirugias: String, tratamiento: String) { // muestra alergenos, cirugias y tratamiento habitual
        tvAlergenosDetalle.text = alergenos
        tvCirugiasPrevias.text = cirugias
        tvTratamientoHabitualMedico.text = tratamiento
    }

    fun mostrarRecetaAnterior(detalle: String) { // muestra el detalle de la receta anterior consultada
        Toast.makeText(this, detalle, Toast.LENGTH_LONG).show()
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}