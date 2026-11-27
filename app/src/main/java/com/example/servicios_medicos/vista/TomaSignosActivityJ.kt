package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.button.MaterialButton
import com.example.servicios_medicos.R
import com.example.servicios_medicos.Data.dataSignosEnfermeriaJ
import com.example.servicios_medicos.presentador.clsSignosEnfermeriaPresentadorJ

class TomaSignosActivityJ : AppCompatActivity() {

    private lateinit var presentador: clsSignosEnfermeriaPresentadorJ

    private lateinit var etTemperatura: EditText
    private lateinit var etFrecuenciaCardiaca: EditText
    private lateinit var etPresionArterial: EditText
    private lateinit var etSaturacion: EditText
    private lateinit var etPeso: EditText
    private lateinit var etFrecuenciaRespiratoria: EditText
    private lateinit var etObservaciones: EditText

    private lateinit var btnEnviarDoctora: MaterialButton

    private var token = ""
    private var correo = ""
    private var matricula = ""
    private var idCita = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_toma_signos)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    return insets
                }
            }
        )

        token = intent.getStringExtra("token").orEmpty()
        correo = intent.getStringExtra("correo").orEmpty()
        matricula = intent.getStringExtra("matricula").orEmpty()
        idCita = intent.getIntExtra("id_cita", 0)

        if (token.isBlank()) {
            mostrarError("Necesitas iniciar sesión como enfermera")
            finish()
            return
        }

        if (matricula.isBlank()) {
            mostrarError("Selecciona un alumno")
            finish()
            return
        }

        etTemperatura = findViewById(R.id.etTemperatura)
        etFrecuenciaCardiaca = findViewById(R.id.etFrecuenciaCardiaca)
        etPresionArterial = findViewById(R.id.etPresionArterial)
        etSaturacion = findViewById(R.id.etSaturacion)
        etPeso = findViewById(R.id.etPeso)
        etFrecuenciaRespiratoria = findViewById(R.id.etFrecuenciaRespiratoria)
        etObservaciones = findViewById(R.id.etObservaciones)

        btnEnviarDoctora = findViewById(R.id.btnEnviarDoctora)

        presentador = clsSignosEnfermeriaPresentadorJ(this)

        mostrarAlumno()
        configurarListeners()
        configurarNavegacion()

        if (idCita == 0) {

            btnEnviarDoctora.text = "Registrar signos sin cita"

            findViewById<TextView>(R.id.txtNota).text =
                "Se guardan los signos del alumno. Sin cita no se incorporan a la cola de consultas de la doctora."
        }
    }

    private fun mostrarAlumno() {

        val nombre = intent.getStringExtra("nombre").orEmpty()
        val academico = intent.getStringExtra("academico") ?: "Cita $idCita"

        findViewById<TextView>(R.id.txtNombrePaciente).text = nombre

        findViewById<TextView>(R.id.txtEdadMatricula).text = "Matrícula $matricula"

        findViewById<TextView>(R.id.txtCarreraGrupo).text = academico

        var iniciales = ""

        for (parte in nombre.split(" ")) {

            if (parte.isNotBlank() && iniciales.length < 2) {

                iniciales += parte.take(1).uppercase()
            }
        }

        findViewById<TextView>(R.id.txtIniciales).text = iniciales

        findViewById<TextView>(R.id.txtRegistrado).text =
            "Pendiente de registrar"
    }

    private fun configurarListeners() {

        btnEnviarDoctora.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    guardar()
                }
            }
        )

        findViewById<View>(R.id.btnRegresar).setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    finish()
                }
            }
        )

        findViewById<View>(R.id.btnAjustes).setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val intent = Intent(
                        this@TomaSignosActivityJ,
                        ConfiguracionActivityC::class.java
                    )

                    intent.putExtra("token", token)
                    intent.putExtra("correo", correo)

                    startActivity(intent)
                }
            }
        )
    }

    private fun guardar() {

        val temperatura = etTemperatura.text.toString().trim()
        val frecuenciaCardiaca = etFrecuenciaCardiaca.text.toString().trim()
        val presion = etPresionArterial.text.toString().trim()
        val saturacion = etSaturacion.text.toString().trim()
        val peso = etPeso.text.toString().trim()
        val frecuenciaRespiratoria = etFrecuenciaRespiratoria.text.toString().trim()
        val observaciones = etObservaciones.text.toString().trim()

        val temperaturaNumerica = temperatura.replace(',', '.').toDoubleOrNull()
        val frecuenciaCardiacaNumerica = frecuenciaCardiaca.toIntOrNull()
        val saturacionNumerica = saturacion.toIntOrNull()
        val pesoNumerico = peso.replace(',', '.').toDoubleOrNull()
        val frecuenciaRespiratoriaNumerica = frecuenciaRespiratoria.toIntOrNull()

        if (temperatura.isNotEmpty() && temperaturaNumerica == null) {

            mostrarError("Temperatura incorrecta")
            return
        }

        if (frecuenciaCardiaca.isNotEmpty() && frecuenciaCardiacaNumerica == null) {

            mostrarError("Frecuencia cardíaca incorrecta")
            return
        }

        if (saturacion.isNotEmpty() && saturacionNumerica == null) {

            mostrarError("Saturación incorrecta")
            return
        }

        if (peso.isNotEmpty() && pesoNumerico == null) {

            mostrarError("Peso incorrecto")
            return
        }

        if (frecuenciaRespiratoria.isNotEmpty() && frecuenciaRespiratoriaNumerica == null) {

            mostrarError("Frecuencia respiratoria incorrecta")
            return
        }

        val datos = dataSignosEnfermeriaJ(
            if (idCita > 0) idCita else null,
            matricula,
            temperaturaNumerica,
            frecuenciaCardiacaNumerica,
            if (presion.isBlank()) null else presion,
            saturacionNumerica,
            pesoNumerico,
            frecuenciaRespiratoriaNumerica,
            observaciones
        )

        presentador.guardar(token, datos)
    }

    private fun configurarNavegacion() {

        val menu = findViewById<BottomNavigationView>(R.id.bnvNavegacion)

        menu.menu.findItem(R.id.nav_campanias)?.isVisible = false

        menu.menu.findItem(R.id.nav_inicio)?.isVisible = false

        menu.selectedItemId = R.id.nav_alumnos

        menu.setOnItemReselectedListener(
            object : NavigationBarView.OnItemReselectedListener {

                override fun onNavigationItemReselected(item: MenuItem) {

                    if (item.itemId == R.id.nav_alumnos) {

                        finish()
                    }
                }
            }
        )
    }

    fun mostrarCargando(cargando: Boolean) {

        btnEnviarDoctora.isEnabled = !cargando

        if (cargando) {

            btnEnviarDoctora.text = "Guardando..."

        } else if (idCita == 0) {

            btnEnviarDoctora.text = "Registrar signos sin cita"

        } else {

            btnEnviarDoctora.text = getString(R.string.toma_signos_enviar_doctora)
        }
    }

    fun mostrarGuardado(mensaje: String) {

        com.example.servicios_medicos.modelo.clsResumenEnfermeriaModeloJ.registrar(token, intent.getStringExtra("nombre").orEmpty())

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    fun mostrarError(mensaje: String) {

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {

        if (::presentador.isInitialized) {

            presentador.cancelar()
        }

        super.onDestroy()
    }
}