package com.example.servicios_medicos.vista

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.adapters.clsMedicamentoRecetaAdaptador
import com.example.servicios_medicos.Data.dataDetalleDiagnosticoJN
import com.example.servicios_medicos.presentador.clsDetalleDiagnosticoPresentadorJN

class DetalleDiagnosticoJN : AppCompatActivity() {

    private lateinit var btnRegresar: ImageButton
    private lateinit var txtFechaConsulta: TextView
    private lateinit var txtNombrePaciente: TextView
    private lateinit var txtMatricula: TextView
    private lateinit var txtFolioReceta: TextView
    private lateinit var txtNombreMedico: TextView
    private lateinit var txtCedulaProfesional: TextView
    private lateinit var txtConsultorio: TextView
    private lateinit var txtEspecialidad: TextView
    private lateinit var txtNombreDiagnostico: TextView
    private lateinit var txtDescripcionDiagnostico: TextView
    private lateinit var rvMedicamentos: RecyclerView

    private lateinit var adapterMedicamentos: clsMedicamentoRecetaAdaptador
    private lateinit var presentador: clsDetalleDiagnosticoPresentadorJN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_diagnostico_jn)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        enlazarVistas()
        btnRegresar.setOnClickListener { finish() }

        presentador = clsDetalleDiagnosticoPresentadorJN(this)

        val token = intent.getStringExtra("token") ?: ""
        val idConsulta = intent.getIntExtra("id_consulta", 0)

        if (token.isNotEmpty() && idConsulta > 0) {
            presentador.cargarDetalleDiagnostico(token, idConsulta)
        } else {
            mostrarError("No se recibió la consulta seleccionada")
        }
    }

    private fun enlazarVistas() {
        btnRegresar = findViewById(R.id.btnRegresar)
        txtFechaConsulta = findViewById(R.id.txtFechaConsulta)
        txtNombrePaciente = findViewById(R.id.txtNombrePaciente)
        txtMatricula = findViewById(R.id.txtMatricula)
        txtFolioReceta = findViewById(R.id.txtFolioReceta)
        txtNombreMedico = findViewById(R.id.txtNombreMedico)
        txtCedulaProfesional = findViewById(R.id.txtCedulaProfesional)
        txtConsultorio = findViewById(R.id.txtConsultorio)
        txtEspecialidad = findViewById(R.id.txtEspecialidad)
        txtNombreDiagnostico = findViewById(R.id.txtNombreDiagnostico)
        txtDescripcionDiagnostico = findViewById(R.id.txtDescripcionDiagnostico)

        rvMedicamentos = findViewById(R.id.rvMedicamentos)
        rvMedicamentos.layoutManager = LinearLayoutManager(this)
        adapterMedicamentos = clsMedicamentoRecetaAdaptador(emptyList())
        rvMedicamentos.adapter = adapterMedicamentos
    }

    fun mostrarDetalleDiagnostico(detalle: dataDetalleDiagnosticoJN) {
        txtFechaConsulta.text = detalle.fechaConsulta
        txtNombrePaciente.text = detalle.nombrePaciente
        txtMatricula.text = "Matrícula ${detalle.matricula}"
        txtFolioReceta.text = "Receta ${detalle.folioReceta}"
        txtNombreMedico.text = detalle.nombreMedico
        txtCedulaProfesional.text = detalle.cedulaProfesional
        txtConsultorio.text = detalle.consultorio
        txtEspecialidad.text = detalle.especialidad
        txtNombreDiagnostico.text = detalle.nombreDiagnostico
        txtDescripcionDiagnostico.text = detalle.descripcionDiagnostico

        adapterMedicamentos.actualizarLista(detalle.medicamentos)
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}