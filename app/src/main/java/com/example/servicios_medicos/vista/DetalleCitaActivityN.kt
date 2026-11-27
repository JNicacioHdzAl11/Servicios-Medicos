package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        presentador = clsDetalleCitaPresentadorN(this)

        configurarListeners()

        val token = intent.getStringExtra("token")
        val idCita = intent.getIntExtra("id_cita", 0)
        presentador.iniciarPantalla(token, idCita)
    }

    private fun configurarListeners() { // dice que hace cada boton
        btnRegresar.setOnClickListener {
            finish()
        }
        btnReprogramarCita.setOnClickListener {
            presentador.reprogramarCita()
        }
        btnCancelarCita.setOnClickListener {
            preguntarCancelar()
        }
    }

    fun mostrarDetalle(tipo: String, estado: String, fecha: String, horario: String, folio: String, medico: String, consultorio: String) { // muestra los datos de la cita
        txtTipoCita.text = tipo
        txtEstadoCita.text = estado
        txtFechaCita.text = fecha
        txtHorarioCita.text = horario
        txtFolioCita.text = folio
        txtMedico.text = medico
        txtConsultorio.text = consultorio
        if (estado == "Pendiente") {
            txtEstadoCita.isSelected = true
        } else {
            txtEstadoCita.isSelected = false
        }
        if (estado == "Cancelada") {
            txtEstadoCita.isActivated = true
        } else {
            txtEstadoCita.isActivated = false
        }
    }

    fun mostrarBotonCancelar(texto: String, activo: Boolean) { // cambia el texto del boton cancelar y lo prende o apaga
        btnCancelarCita.text = texto
        btnCancelarCita.isEnabled = activo
    }

    fun mostrarVolverAAgendar(texto: String) { // quita cancelar y cambia reprogramar por volver a agendar
        btnCancelarCita.visibility = View.GONE
        btnReprogramarCita.text = texto
    }

    fun preguntarCancelar() { // abre una ventana para preguntar si quiere cancelar
        val diseno = layoutInflater.inflate(R.layout.ventana_cancelar_cita_n, null)
        val btnNoCancelar: MaterialButton = diseno.findViewById(R.id.btnNoCancelar)
        val btnSiCancelar: MaterialButton = diseno.findViewById(R.id.btnSiCancelar)
        val ventana = AlertDialog.Builder(this).setView(diseno).create()
        val fondo = ventana.window
        if (fondo != null) {
            fondo.setBackgroundDrawableResource(android.R.color.transparent)
        }
        btnNoCancelar.setOnClickListener {
            ventana.dismiss()
        }
        btnSiCancelar.setOnClickListener {
            ventana.dismiss()
            presentador.confirmarCancelacion()
        }
        ventana.show()
    }

    fun abrirAgendarCita(token: String, motivo: String) { // abre agendar cita con el motivo de esta cita
        val intent = Intent(this, AgendarCitaActivityN::class.java)
        intent.putExtra("token", token)
        intent.putExtra("motivo", motivo)
        startActivity(intent)
    }

    fun mostrarMensaje(mensaje: String) { // muestra un aviso al alumno
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
