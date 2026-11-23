package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsDetalleCitaPresentadorN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class DetalleCitaActivityN : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtTipoCita: TextView
    private lateinit var txtEstadoCita: TextView
    private lateinit var txtFechaCita: TextView
    private lateinit var txtHorarioCita: TextView
    private lateinit var txtFolioCita: TextView
    private lateinit var txtMedico: TextView
    private lateinit var txtConsultorio: TextView
    private lateinit var btnReprogramarCita: MaterialButton
    private lateinit var btnCancelarCita: MaterialButton
    private lateinit var bnvNavegacion: BottomNavigationView
    private lateinit var presentador: clsDetalleCitaPresentadorN

    override fun onCreate(savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca detalle cita
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_cita_n)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegresar = findViewById(R.id.ibRegresar)
        txtTipoCita = findViewById(R.id.tvTipoCita)
        txtEstadoCita = findViewById(R.id.tvEstadoCita)
        txtFechaCita = findViewById(R.id.tvFechaCita)
        txtHorarioCita = findViewById(R.id.tvHorarioCita)
        txtFolioCita = findViewById(R.id.tvFolioCita)
        txtMedico = findViewById(R.id.tvMedico)
        txtConsultorio = findViewById(R.id.tvConsultorio)
        btnReprogramarCita = findViewById(R.id.btnReprogramarCita)
        btnCancelarCita = findViewById(R.id.btnCancelarCita)
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        presentador = clsDetalleCitaPresentadorN(this)

        configurarListeners()

        val token = intent.getStringExtra("token")
        val idCita = intent.getIntExtra("id_cita", 0)
        presentador.iniciarPantalla(token, idCita)
    }

    private fun configurarListeners() { // dice que hace cada boton y el menu de abajo
        btnRegresar.setOnClickListener {
            finish()
        }
        btnReprogramarCita.setOnClickListener {
            presentador.reprogramarCita()
        }
        btnCancelarCita.setOnClickListener {
            presentador.cancelarCita()
        }
        bnvNavegacion.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_inicio) {
                irAPantallaPrincipal("inicio")
            }
            if (item.itemId == R.id.nav_citas) {
                irAPantallaPrincipal("citas")
            }
            false
        }
        bnvNavegacion.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.nav_citas) {
                irAPantallaPrincipal("citas")
            }
        }
    }

    private fun irAPantallaPrincipal(abrir: String) { // regresa a la pantalla principal y abre inicio o mis citas
        val intent = Intent(this, AlumnoMainActivityN::class.java)
        intent.putExtra("abrir", abrir)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun mostrarDetalle(tipo: String, estado: String, fecha: String, horario: String, folio: String, medico: String, consultorio: String) { // muestra los datos de la cita
        txtTipoCita.text = tipo
        txtEstadoCita.text = estado
        txtFechaCita.text = fecha
        txtHorarioCita.text = horario
        txtFolioCita.text = folio
        txtMedico.text = medico
        txtConsultorio.text = consultorio
    }

    fun activarCancelar(activo: Boolean) { // activa o apaga el boton cancelar cita
        btnCancelarCita.isEnabled = activo
        if (activo) {
            btnCancelarCita.alpha = 1f
        } else {
            btnCancelarCita.alpha = 0.4f
        }
    }

    fun preguntarCancelar() { // abre una ventana para preguntar si quiere cancelar
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Cancelar cita")
        ventana.setMessage("¿Seguro que quieres cancelar esta cita?")
        ventana.setPositiveButton("Sí, cancelar") { dialogo, boton ->
            presentador.confirmarCancelacion()
        }
        ventana.setNegativeButton("No", null)
        ventana.show()
    }

    fun mostrarCargando(cargando: Boolean) { // cambia el boton cancelar mientras se cancela la cita
        if (cargando) {
            btnCancelarCita.isEnabled = false
            btnCancelarCita.text = "Cancelando..."
        } else {
            btnCancelarCita.text = "Cancelar cita"
        }
    }

    fun abrirAgendarCita(token: String) { // abre agendar cita
        val intent = Intent(this, AgendarCitaActivityN::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    fun mostrarMensaje(mensaje: String) { // muestra un aviso cuando la cita se cancelo
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}

