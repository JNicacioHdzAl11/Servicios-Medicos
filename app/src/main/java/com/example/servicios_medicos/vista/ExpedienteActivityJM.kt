package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.ConsultaRecienteModelo
import com.example.servicios_medicos.modelo.ExpedienteAlumnoData
import com.example.servicios_medicos.presentador.clsExpedienteAlumnoPresentadorJM
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExpedienteAlumnoActivityJM : AppCompatActivity() {

    private lateinit var btnVolver: ImageButton
    private lateinit var btnConfiguracion: ImageButton
    private lateinit var tvNombreAlumno: TextView
    private lateinit var tvMatriculaActualizacion: TextView
    private lateinit var tvTipoSangre: TextView
    private lateinit var tvAlergias: TextView
    private lateinit var tvPadecimientos: TextView
    private lateinit var tvCirugias: TextView
    private lateinit var tvTratamientoHabitual: TextView
    private lateinit var tvVerHistorial: TextView
    private lateinit var tvDescargarReceta1: TextView
    private lateinit var tvDescargarReceta2: TextView
    private lateinit var bnvNavegacion: BottomNavigationView

    private lateinit var presenter: clsExpedienteAlumnoPresentadorJM
    private var tokenSesion: String = ""
    private var consultasActuales: List<ConsultaRecienteModelo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expediente_jm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver = findViewById(R.id.btnVolver)
        btnConfiguracion = findViewById(R.id.btnConfiguracion)
        tvNombreAlumno = findViewById(R.id.tvNombreAlumno)
        tvMatriculaActualizacion = findViewById(R.id.tvMatriculaActualizacion)
        tvTipoSangre = findViewById(R.id.tvTipoSangre)
        tvAlergias = findViewById(R.id.tvAlergias)
        tvPadecimientos = findViewById(R.id.tvPadecimientos)
        tvCirugias = findViewById(R.id.tvCirugias)
        tvTratamientoHabitual = findViewById(R.id.tvTratamientoHabitual)
        tvVerHistorial = findViewById(R.id.tvVerHistorial)
        tvDescargarReceta1 = findViewById(R.id.tvDescargarReceta1)
        tvDescargarReceta2 = findViewById(R.id.tvDescargarReceta2)
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        tokenSesion = intent.getStringExtra("token") ?: ""
        presenter = clsExpedienteAlumnoPresentadorJM(this)

        if (tokenSesion.isNotEmpty()) {
            presenter.cargarExpediente(tokenSesion)
        } else {
            mostrarError("No se recibió token de sesión")
        }

        btnVolver.setOnClickListener {
            finish()
        }

        btnConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivityC::class.java))
        }

        tvVerHistorial.setOnClickListener(this::verHistorial)
        tvDescargarReceta1.setOnClickListener(this::descargarReceta1)
        tvDescargarReceta2.setOnClickListener(this::descargarReceta2)

        bnvNavegacion.selectedItemId = R.id.nav_expediente
        bnvNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    startActivity(Intent(this, AlumnoMainActivityN::class.java))
                    true
                }
                R.id.nav_expediente -> true
                R.id.nav_citas, R.id.nav_campanias, R.id.nav_perfil -> {
                    Toast.makeText(this, "Sección ${item.title}", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    fun verHistorial(v: View) {
        if (tokenSesion.isNotEmpty()) {
            presenter.cargarHistorial(tokenSesion)
        } else {
            mostrarError("No se recibió token de sesión")
        }
    }

    fun descargarReceta1(v: View) {
        val consulta = consultasActuales.getOrNull(0)
        if (consulta != null && tokenSesion.isNotEmpty()) {
            presenter.descargarReceta(tokenSesion, consulta.idConsulta)
        } else {
            mostrarError("Sin receta disponible para esta consulta")
        }
    }

    fun descargarReceta2(v: View) {
        val consulta = consultasActuales.getOrNull(1)
        if (consulta != null && tokenSesion.isNotEmpty()) {
            presenter.descargarReceta(tokenSesion, consulta.idConsulta)
        } else {
            mostrarError("Sin receta disponible para esta consulta")
        }
    }

    fun mostrarExpediente(datos: ExpedienteAlumnoData) {
        val exp = datos.expediente
        if (exp != null) {
            if (exp.nombreCompleto.isNotEmpty()) tvNombreAlumno.text = exp.nombreCompleto
            if (exp.matricula.isNotEmpty() || exp.fechaActualizacion.isNotEmpty()) {
                tvMatriculaActualizacion.text =
                    "Matrícula ${exp.matricula} · Actualizado el ${exp.fechaActualizacion}"
            }
            if (exp.tipoSangre.isNotEmpty()) tvTipoSangre.text = exp.tipoSangre
            if (exp.alergias.isNotEmpty()) tvAlergias.text = exp.alergias
            if (exp.padecimientos.isNotEmpty()) tvPadecimientos.text = exp.padecimientos
            if (exp.cirugias.isNotEmpty()) tvCirugias.text = exp.cirugias
            if (exp.tratamientoHabitual.isNotEmpty()) {
                tvTratamientoHabitual.text = exp.tratamientoHabitual
            }
        }
        consultasActuales = datos.consultasRecientes
    }

    fun mostrarHistorial(consultas: List<ConsultaRecienteModelo>) {
        consultasActuales = consultas
        Toast.makeText(this, "Historial cargado (${consultas.size} consultas)", Toast.LENGTH_SHORT).show()
    }

    fun mostrarDetalleReceta(medicamentos: String, indicaciones: String, urlPdf: String) {
        val resumen = if (medicamentos.isNotEmpty()) {
            "Receta: $medicamentos — $indicaciones"
        } else if (urlPdf.isNotEmpty()) {
            "Receta disponible: $urlPdf"
        } else {
            "Receta consultada correctamente"
        }
        Toast.makeText(this, resumen, Toast.LENGTH_LONG).show()
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}