package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsConsultaMedicaPresentadorJM
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class ConsultaMedicaActivityJM : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtTitulo: TextView
    private lateinit var ivAvatarMedico: ShapeableImageView

    private lateinit var txtNombreConsulta: TextView
    private lateinit var txtDetalleAlumnoConsulta: TextView
    private lateinit var txtAlergiaConsulta: TextView

    private lateinit var txtRegistroSignos: TextView
    private lateinit var txtPresionArterial: TextView
    private lateinit var txtTemperatura: TextView
    private lateinit var txtFrecuenciaCardiaca: TextView
    private lateinit var txtSpO2: TextView

    private lateinit var edtMotivoConsulta: TextInputEditText
    private lateinit var edtPadecimientoActual: TextInputEditText
    private lateinit var edtSintomasManifestados: TextInputEditText
    private lateinit var edtDiagnosticoMedico: TextInputEditText

    private lateinit var btnGuardarGenerarReceta: MaterialButton
    private lateinit var txtNotaConsulta: TextView
    private lateinit var presentador: clsConsultaMedicaPresentadorJM

    override fun onCreate(savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca la consulta medica
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_medica_jm)

        btnRegresar = findViewById(R.id.btnRegresar)
        txtTitulo = findViewById(R.id.txtTitulo)
        ivAvatarMedico = findViewById(R.id.ivAvatarMedico)

        txtNombreConsulta = findViewById(R.id.txtNombreConsulta)
        txtDetalleAlumnoConsulta = findViewById(R.id.txtDetalleAlumnoConsulta)
        txtAlergiaConsulta = findViewById(R.id.txtAlergiaConsulta)

        txtRegistroSignos = findViewById(R.id.txtRegistroSignos)
        txtPresionArterial = findViewById(R.id.txtPresionArterial)
        txtTemperatura = findViewById(R.id.txtTemperatura)
        txtFrecuenciaCardiaca = findViewById(R.id.txtFrecuenciaCardiaca)
        txtSpO2 = findViewById(R.id.txtSpO2)

        edtMotivoConsulta = findViewById(R.id.edtMotivoConsulta)
        edtPadecimientoActual = findViewById(R.id.edtPadecimientoActual)
        edtSintomasManifestados = findViewById(R.id.edtSintomasManifestados)
        edtDiagnosticoMedico = findViewById(R.id.edtDiagnosticoMedico)

        btnGuardarGenerarReceta = findViewById(R.id.btnGuardarGenerarReceta)
        txtNotaConsulta = findViewById(R.id.txtNotaConsulta)

        configurarListeners()

        presentador = clsConsultaMedicaPresentadorJM(this)
        presentador.iniciarPantalla(
            intent.getStringExtra("token"),
            intent.getIntExtra("id_consulta", 0),
            intent.getStringExtra("matricula")
        )
    }

    private fun configurarListeners() { // define que hace cada boton en la pantalla de consulta
        btnRegresar.setOnClickListener {
            finish()
        }

        btnGuardarGenerarReceta.setOnClickListener {
            presentador.guardarYGenerarReceta(
                motivo = edtMotivoConsulta.text.toString(),
                padecimiento = edtPadecimientoActual.text.toString(),
                sintomas = edtSintomasManifestados.text.toString(),
                diagnostico = edtDiagnosticoMedico.text.toString()
            )
        }
    }

    override fun onResume() { // carga los datos cada vez que se muestra la pantalla
        super.onResume()
        presentador.cargarDetalleConsulta()
    }

    fun mostrarResumenAlumno(nombre: String, detalle: String, alergia: String) { // muestra los datos generales y alergia del alumno
        if (nombre.isNotEmpty()) {
            txtNombreConsulta.text = nombre
        }
        if (detalle.isNotEmpty()) {
            txtDetalleAlumnoConsulta.text = detalle
        }
        txtAlergiaConsulta.text = alergia
    }

    fun mostrarSignosVitales( // muestra los signos vitales tomados en enfermeria
        registro: String,
        presion: String,
        temperatura: String,
        frecuencia: String,
        spo2: String
    ) {
        if (registro.isNotEmpty()) {
            txtRegistroSignos.text = registro
        }
        txtPresionArterial.text = presion
        txtTemperatura.text = temperatura
        txtFrecuenciaCardiaca.text = frecuencia
        txtSpO2.text = spo2
    }

    fun mostrarCamposClinicos( // rellena los campos clinicos si ya existen datos guardados
        motivo: String,
        padecimiento: String,
        sintomas: String,
        diagnostico: String
    ) {
        if (motivo.isNotEmpty()) {
            edtMotivoConsulta.setText(motivo)
        }
        if (padecimiento.isNotEmpty()) {
            edtPadecimientoActual.setText(padecimiento)
        }
        if (sintomas.isNotEmpty()) {
            edtSintomasManifestados.setText(sintomas)
        }
        if (diagnostico.isNotEmpty()) {
            edtDiagnosticoMedico.setText(diagnostico)
        }
    }

    fun abrirReceta(idConsulta: Int, matricula: String) { // pasa a la pantalla de receta con los datos de la consulta
        Toast.makeText(this, "Consulta guardada correctamente.", Toast.LENGTH_SHORT).show()
        val destino = Intent(this, RecetaActivityJM::class.java)
        destino.putExtra("token", intent.getStringExtra("token"))
        destino.putExtra("id_consulta", idConsulta)
        destino.putExtra("matricula", matricula)
        destino.putExtra("nombre_paciente", txtNombreConsulta.text.toString())
        destino.putExtra("alergia", txtAlergiaConsulta.text.toString())
        startActivity(destino)
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando ocurre algun error
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}