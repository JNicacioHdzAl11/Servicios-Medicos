package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.ConsultaMedicaData
import com.example.servicios_medicos.presentador.clsConsultaMedicaPresentadorJM
import com.google.android.material.bottomnavigation.BottomNavigationView

class ConsultaMedicaActivityJM : AppCompatActivity() {

    private lateinit var btnVolver: ImageView
    private lateinit var tvTitulo: TextView
    private lateinit var ivMiniAvatar: ImageView
    private lateinit var tvNombreConsulta: TextView
    private lateinit var tvDetalleAlumnoConsulta: TextView
    private lateinit var tvAlergiaConsulta: TextView
    private lateinit var tvRegistroSignos: TextView
    private lateinit var tvPresionArterial: TextView
    private lateinit var tvTemperatura: TextView
    private lateinit var tvFrecuenciaCardiaca: TextView
    private lateinit var tvSpO2: TextView
    private lateinit var etMotivoConsulta: EditText
    private lateinit var etPadecimientoActual: EditText
    private lateinit var etSintomasManifestados: EditText
    private lateinit var etDiagnosticoMedico: EditText
    private lateinit var btnGuardarGenerarReceta: Button
    private lateinit var bnvNavegacion: BottomNavigationView

    private lateinit var presenter: clsConsultaMedicaPresentadorJM
    private var tokenSesion: String = ""
    private var matriculaAlumno: String = ""
    private var idCitaActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consulta_medica_jm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}