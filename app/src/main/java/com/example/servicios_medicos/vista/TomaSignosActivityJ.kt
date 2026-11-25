package com.example.servicios_medicos.vista

import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import android.view.MenuItem

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.EditText
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.*
import com.example.servicios_medicos.presentador.*
import com.google.android.material.button.MaterialButton
class TomaSignosActivityJ : AppCompatActivity() {
    private var token = ""
    private var correo = ""

    private fun preparar(layout: Int): Boolean {
        enableEdgeToEdge()
        setContentView(layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
                    return insets
                }
            })
        token = intent.getStringExtra("token").orEmpty()
        correo = intent.getStringExtra("correo").orEmpty()
        if (token.isBlank()) {
            mostrarError("Necesitas iniciar sesión como enfermera")
            finish()
            return false
        }
        return true
    }

    private fun destino(clase: Class<*>): Intent {
        val salida = Intent(this, clase)
        salida.putExtra("token", token)
        salida.putExtra("correo", correo)
        return salida
    }

    private fun conectarAjustes() {
        findViewById<View>(R.id.btnAjustes).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(destino(ConfiguracionActivityC::class.java))
            }
        })
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private lateinit var presentador: clsSignosEnfermeriaPresentadorJ
    private lateinit var boton: MaterialButton
    private var matricula = ""
    private var idCita = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!preparar(R.layout.activity_toma_signos)) return
        matricula = intent.getStringExtra("matricula").orEmpty(); idCita = intent.getIntExtra("id_cita", 0)
        if (matricula.isBlank()) { mostrarError("Selecciona un alumno"); finish(); return }
        val nombre = intent.getStringExtra("nombre").orEmpty()
        findViewById<TextView>(R.id.txtNombrePaciente).text = nombre
        findViewById<TextView>(R.id.txtEdadMatricula).text = "Matrícula $matricula"
        findViewById<TextView>(R.id.txtCarreraGrupo).text = intent.getStringExtra("academico") ?: "Cita $idCita"
        var iniciales = ""
        for (parte in nombre.split(" ")) if (parte.isNotBlank() && iniciales.length < 2) iniciales += parte.take(1).uppercase()
        findViewById<TextView>(R.id.txtIniciales).text = iniciales
        findViewById<TextView>(R.id.txtRegistrado).text = "Pendiente de registrar"
        boton = findViewById(R.id.btnEnviarDoctora)
        if (idCita == 0) {
            boton.text = "Registrar signos sin cita"
            findViewById<TextView>(R.id.txtNota).text = "Se guardan los signos del alumno. Sin cita no se incorporan a la cola de consultas de la doctora."
        }
        presentador = clsSignosEnfermeriaPresentadorJ(this)
        conectarAjustes()
        conectarNavegacion()
        findViewById<View>(R.id.btnRegresar).setOnClickListener(object : View.OnClickListener { override fun onClick(v: View?) { finish() } })
        boton.setOnClickListener(object : View.OnClickListener { override fun onClick(v: View?) { guardar() } })
    }
    private fun conectarNavegacion() {
        val menu = findViewById<BottomNavigationView>(R.id.bnvNavegacion)
        menu.menu.findItem(R.id.nav_campanias)?.isVisible = false
        menu.menu.findItem(R.id.nav_inicio)?.isVisible = false
        menu.selectedItemId = R.id.nav_alumnos
        menu.setOnItemReselectedListener(object : NavigationBarView.OnItemReselectedListener {
            override fun onNavigationItemReselected(item: MenuItem) {
                if (item.itemId == R.id.nav_alumnos) finish()
            }
        })
    }

    private fun texto(id: Int): String = findViewById<EditText>(id).text.toString().trim()
    private fun guardar() {
        val temperatura = texto(R.id.etTemperatura); val fc = texto(R.id.etFrecuenciaCardiaca)
        val presion = texto(R.id.etPresionArterial); val spo2 = texto(R.id.etSaturacion)
        val peso = texto(R.id.etPeso); val fr = texto(R.id.etFrecuenciaRespiratoria)
        if ((temperatura.isNotEmpty() && temperatura.replace(',', '.').toDoubleOrNull() == null) || (peso.isNotEmpty() && peso.replace(',', '.').toDoubleOrNull() == null) || (fc.isNotEmpty() && fc.toIntOrNull() == null) || (fr.isNotEmpty() && fr.toIntOrNull() == null) || (spo2.isNotEmpty() && spo2.toIntOrNull() == null)) { mostrarError("Revisa los valores numéricos"); return }
        val datos = dataSignosEnfermeriaJ(if (idCita > 0) idCita else null, matricula, temperatura.replace(',', '.').toDoubleOrNull(), fc.toIntOrNull(), if (presion.isBlank()) null else presion, spo2.toIntOrNull(), peso.replace(',', '.').toDoubleOrNull(), fr.toIntOrNull(), texto(R.id.etObservaciones))
        presentador.guardar(token, datos)
    }
    fun mostrarCargando(cargando: Boolean) { boton.isEnabled = !cargando; boton.text = if (cargando) "Guardando..." else if (idCita == 0) "Registrar signos sin cita" else getString(R.string.toma_signos_enviar_doctora) }
    fun mostrarGuardado(mensaje: String) { Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show(); finish() }
    override fun onDestroy() { if (::presentador.isInitialized) presentador.cancelar(); super.onDestroy() }
}
