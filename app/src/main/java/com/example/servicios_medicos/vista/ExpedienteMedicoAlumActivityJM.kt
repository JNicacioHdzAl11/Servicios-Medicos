package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsExpedienteMedicoAlumnoPresentadorJM
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView

class ExpedienteMedicoAlumActivityJM : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtTitulo: TextView
    private lateinit var ivAvatarMedico: ShapeableImageView
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
    private lateinit var rvHistorialConsultas: RecyclerView
    private lateinit var btnIniciarNuevaConsulta: MaterialButton
    private lateinit var txtNotaExpedienteMed: TextView
    private lateinit var presentador: clsExpedienteMedicoAlumnoPresentadorJM
    private var token = ""
    private var matricula = ""
    private var idConsulta = 0

    override fun onCreate(savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca el expediente medico del alumno
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expediente_jm)

        btnRegresar = findViewById(R.id.btnRegresar)
        txtTitulo = findViewById(R.id.txtTitulo)
        ivAvatarMedico = findViewById(R.id.ivAvatarMedico)
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

        configurarListeners()

        presentador = clsExpedienteMedicoAlumnoPresentadorJM(this)
        val tokenRecibido = intent.getStringExtra("token")
        if (tokenRecibido != null) {
            token = tokenRecibido
        }
        val matriculaRecibida = intent.getStringExtra("matricula")
        if (matriculaRecibida != null) {
            matricula = matriculaRecibida
        }
        idConsulta = intent.getIntExtra("id_consulta", 0)

        presentador.iniciarPantalla(token, matricula)
    }

    private fun configurarListeners() { // dice que hace cada boton de la pantalla
        btnRegresar.setOnClickListener {
            finish()
        }

    }

    override fun onResume() { // carga los datos cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarExpedienteMedico()
    }

    fun mostrarIniciales(texto: String) { // muestra las iniciales en el circulo del alumno
        txtIniciales.text = texto
    }

    fun mostrarNombrePaciente(nombre: String) { // muestra el nombre completo del alumno
        txtNombrePaciente.text = nombre
    }

    fun mostrarMatriculaEdad(texto: String) { // muestra la matricula y la edad
        txtMatriculaEdad.text = texto
    }

    fun mostrarCarreraGrupo(texto: String) { // muestra la carrera, cuatrimestre y grupo
        txtCarreraGrupo.text = texto
    }

    fun mostrarAlertaAlergia(texto: String) { // muestra el banner rojo de alergias
        txtAlertaAlergia.text = texto
    }

    fun mostrarResumenClinico(tipoSangre: String, enfermedades: String) { // muestra tipo de sangre y enfermedades cronicas
        txtTipoSangre.text = tipoSangre
        txtEnfermedadesCronicas.text = enfermedades
    }

    fun mostrarAntecedentes(alergenos: String, cirugias: String, tratamiento: String) { // muestra alergenos, cirugias previas y tratamiento habitual
        txtAlergenosDetalle.text = alergenos
        txtCirugiasPrevias.text = cirugias
        txtTratamientoHabitual.text = tratamiento
    }

    fun mostrarHistorialConsultas(consultas: List<Any>) { // actualiza la lista de consultas recientes en el RecyclerView
        rvHistorialConsultas.tag = consultas
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}