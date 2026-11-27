package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.Data.dataPerfilConfigDoctoraJM
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsConfigDoctoraPresentadorJM
import com.google.android.material.materialswitch.MaterialSwitch

class ConfiguracionDoctoraActivityJM : AppCompatActivity() {
    private lateinit var presentador: clsConfigDoctoraPresentadorJM
    private lateinit var txtCorreoDoctora: TextView
    private lateinit var txtCedulaFirma: TextView
    private lateinit var modoOscuro: MaterialSwitch
    private lateinit var avisosUrgencias: MaterialSwitch
    private var token = ""
    private var actualizandoVista = false
    private var configuracionCargada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion_doctora_jm)
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
        if (token.isBlank()) {
            mostrarError("Necesitas iniciar sesión")
            finish()
            return
        }

        txtCorreoDoctora = findViewById(R.id.tvCorreoInstitucional)
        txtCedulaFirma = findViewById(R.id.tvCedulaFirma)
        modoOscuro = findViewById(R.id.swModoOscuro)
        avisosUrgencias = findViewById(R.id.swAvisosUrgencias)
        presentador = clsConfigDoctoraPresentadorJM(this)

        findViewById<View>(R.id.ibAjustes).visibility = View.INVISIBLE

        txtCorreoDoctora.text = intent.getStringExtra("correo") ?: "Sin correo registrado"
        txtCedulaFirma.text = "Consultando..."
        findViewById<TextView>(R.id.tvModoOscuro).text = "Consultando..."
        findViewById<TextView>(R.id.tvAvisosUrgencias).text = "Consultando..."
        modoOscuro.isEnabled = false
        avisosUrgencias.isEnabled = false

        configurarListeners()
    }

    override fun onResume() {
        super.onResume()
        if (::presentador.isInitialized) {
            presentador.cargarConfiguracion(token)
        }
    }

    private fun configurarListeners() {
        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (v?.id) {
                    R.id.ibRegresar -> onBackPressedDispatcher.onBackPressed()
                    R.id.clCambiarContrasena -> {
                        val destino = Intent(this@ConfiguracionDoctoraActivityJM, AjustesContrasenaFragmentC::class.java)
                        destino.putExtra("token", token)
                        startActivity(destino)
                    }
                    R.id.clProtocolosMedicos -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, TerminosActivityC::class.java))
                    R.id.clPrivacidadPaciente -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, PrivacidadActivityC::class.java))
                    R.id.clSoporteTecnico -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, PreguntasFrecuentesActivityC::class.java))
                    R.id.btnCerrarSesion -> cerrarSesion()
                }
            }
        }

        findViewById<View>(R.id.ibRegresar).setOnClickListener(listener)
        findViewById<View>(R.id.clCambiarContrasena).setOnClickListener(listener)
        findViewById<View>(R.id.clProtocolosMedicos).setOnClickListener(listener)
        findViewById<View>(R.id.clPrivacidadPaciente).setOnClickListener(listener)
        findViewById<View>(R.id.clSoporteTecnico).setOnClickListener(listener)
        findViewById<View>(R.id.btnCerrarSesion).setOnClickListener(listener)

        val preferencias = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (!actualizandoVista && configuracionCargada) {
                    presentador.guardarPreferencias(token, modoOscuro.isChecked, avisosUrgencias.isChecked)
                }
            }
        }
        modoOscuro.setOnCheckedChangeListener(preferencias)
        avisosUrgencias.setOnCheckedChangeListener(preferencias)
    }

    private fun cerrarSesion() {
        val destino = Intent(this, LoginActivityC::class.java)
        destino.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(destino)
        finish()
    }

    fun mostrarConfiguracion(datos: dataPerfilConfigDoctoraJM) {
        txtCorreoDoctora.text = if (datos.correo.isBlank()) "Sin correo registrado" else datos.correo
        txtCedulaFirma.text = if (datos.cedulaFirma.isBlank()) "Sin cédula registrada" else datos.cedulaFirma
        configuracionCargada = true
        actualizarPreferencias(datos.modoOscuro, datos.avisosUrgencias)
        mostrarCargando(false)
    }

    private fun actualizarPreferencias(oscuro: Boolean, urgencias: Boolean) {
        actualizandoVista = true
        modoOscuro.isChecked = oscuro
        avisosUrgencias.isChecked = urgencias
        actualizandoVista = false
        findViewById<TextView>(R.id.tvModoOscuro).text = if (oscuro) "Activado" else "Desactivado"
        findViewById<TextView>(R.id.tvAvisosUrgencias).text = if (urgencias) "Activados" else "Desactivados"
    }

    fun mostrarCargando(cargando: Boolean) {
        modoOscuro.isEnabled = !cargando && configuracionCargada
        avisosUrgencias.isEnabled = !cargando && configuracionCargada
    }

    fun mostrarGuardando(guardando: Boolean) {
        mostrarCargando(guardando)
    }

    fun mostrarPreferenciasGuardadas(oscuroGuardado: Boolean, urgenciasGuardadas: Boolean) {
        actualizarPreferencias(oscuroGuardado, urgenciasGuardadas)
        Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
    }

    fun mostrarError(mensaje: String) {
        if (::txtCedulaFirma.isInitialized && !configuracionCargada) {
            txtCedulaFirma.text = "No disponible"
            findViewById<TextView>(R.id.tvModoOscuro).text = "No disponible"
            findViewById<TextView>(R.id.tvAvisosUrgencias).text = "No disponible"
        }
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        if (::presentador.isInitialized) {
            presentador.cancelar()
        }
        super.onDestroy()
    }
}