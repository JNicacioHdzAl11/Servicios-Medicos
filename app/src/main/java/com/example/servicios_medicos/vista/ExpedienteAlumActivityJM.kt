package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.ConsultaMedicoModelo
import com.example.servicios_medicos.modelo.ExpedienteMedicoData
import com.example.servicios_medicos.modelo.clsExpedienteAlumnoModeloJM
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

    private lateinit var presenter: clsExpedienteAlumPresentadorJM
    private var tokenSesion: String = ""
    private var matriculaActual: String = ""
    private var consultasRecientes: List<ConsultaMedicoModelo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
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

        tokenSesion = intent.getStringExtra("token") ?: ""
        matriculaActual = intent.getStringExtra("matricula") ?: ""

        presenter = clsExpedienteAlumPresentadorJM(this)

        if (tokenSesion.isNotEmpty() && matriculaActual.isNotEmpty()) {
            presenter.realizarBusqueda(tokenSesion, matriculaActual)
        }

        tvVerRecetaAnterior1.setOnClickListener(this::verReceta1)
        tvVerRecetaAnterior2.setOnClickListener(this::verReceta2)
        btnIniciarNuevaConsulta.setOnClickListener(this::iniciarNuevaConsulta)

        btnVolver.setOnClickListener {
            finish()
        }

        bnvNavegacion.setOnItemSelectedListener { item ->
            Toast.makeText(this, "Sección ${item.title}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun buscarDatos(t: TextView?, boton: Int, evento: KeyEvent?): Boolean {
        if (boton == EditorInfo.IME_ACTION_DONE) {
            presenter.realizarBusqueda(tokenSesion, t?.text?.toString()?.trim().orEmpty())
        }
        return false
    }

    fun mostrarExpedienteAlumno(datos: ExpedienteMedicoData) {
        if (datos.iniciales.isNotEmpty()) tvIniciales.text = datos.iniciales
        if (datos.nombrePaciente.isNotEmpty()) tvNombrePaciente.text = datos.nombrePaciente
        if (datos.matricula.isNotEmpty()) matriculaActual = datos.matricula
        if (datos.matricula.isNotEmpty() || datos.edad.isNotEmpty()) {
            tvMatriculaEdad.text = "Matrícula ${datos.matricula} · ${datos.edad}"
        }
        if (datos.carreraGrupo.isNotEmpty()) tvCarreraGrupo.text = datos.carreraGrupo
        if (datos.alergia.isNotEmpty()) tvAlertaAlergia.text = "Alergia: ${datos.alergia}"
        if (datos.tipoSangre.isNotEmpty()) tvTipoSangreMedico.text = datos.tipoSangre
        if (datos.enfermedadesCronicas.isNotEmpty()) {
            tvEnfermedadesCronicas.text = datos.enfermedadesCronicas
        }
        if (datos.alergenosDetalle.isNotEmpty()) tvAlergenosDetalle.text = datos.alergenosDetalle
        if (datos.cirugiasPrevias.isNotEmpty()) tvCirugiasPrevias.text = datos.cirugiasPrevias
        if (datos.tratamientoHabitual.isNotEmpty()) {
            tvTratamientoHabitualMedico.text = datos.tratamientoHabitual
        }
        consultasRecientes = datos.consultasRecientes
    }

    fun verReceta1(v: View) {
        val idConsulta = consultasRecientes.getOrNull(0)?.idConsulta ?: 0
        if (tokenSesion.isNotEmpty() && idConsulta > 0) {
            presenter.consultarRecetaAnterior(tokenSesion, idConsulta)
        } else {
            Toast.makeText(this, "Receta anterior 1", Toast.LENGTH_SHORT).show()
        }
    }

    fun verReceta2(v: View) {
        val idConsulta = consultasRecientes.getOrNull(1)?.idConsulta ?: 0
        if (tokenSesion.isNotEmpty() && idConsulta > 0) {
            presenter.consultarRecetaAnterior(tokenSesion, idConsulta)
        } else {
            Toast.makeText(this, "Receta anterior 2", Toast.LENGTH_SHORT).show()
        }
    }

    fun iniciarNuevaConsulta(v: View) {
        val intent = Intent(this, ConsultaMedicaActivityJM::class.java).apply {
            putExtra("token", tokenSesion)
            putExtra("matricula", matriculaActual)
        }
        startActivity(intent)
    }

    fun recetaCargada(medicamentos: String, indicaciones: String) {
        Toast.makeText(this, "$medicamentos · $indicaciones", Toast.LENGTH_LONG).show()
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}