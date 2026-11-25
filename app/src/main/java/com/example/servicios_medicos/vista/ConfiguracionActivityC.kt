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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.dataPerfilConfiguracionC
import com.example.servicios_medicos.presentador.clsConfiguracionPresentadorC
import com.google.android.material.materialswitch.MaterialSwitch

class ConfiguracionActivityC : AppCompatActivity() {
    private lateinit var presentador: clsConfiguracionPresentadorC
    private lateinit var txtCorreo: TextView
    private lateinit var txtEmergencia: TextView
    private lateinit var recordatorios: MaterialSwitch
    private lateinit var campanias: MaterialSwitch
    private lateinit var contenedor: View
    private var token = ""
    private var actualizandoVista = false
    private var configuracionCargada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
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

        txtCorreo = findViewById(R.id.tvCorreoInstitucional)
        txtEmergencia = findViewById(R.id.tvTelefonoEmergencia)
        recordatorios = findViewById(R.id.swRecordatoriosCitas)
        campanias = findViewById(R.id.swAvisosCampanias)
        contenedor = findViewById(R.id.contenedorAjustesC)
        presentador = clsConfiguracionPresentadorC(this)

        findViewById<View>(R.id.clModoOscuro).visibility = View.GONE
        findViewById<View>(R.id.vDivisorCuenta3).visibility = View.GONE
        findViewById<View>(R.id.ibAjustes).visibility = View.INVISIBLE

        txtCorreo.text = intent.getStringExtra("correo") ?: "Sin correo registrado"
        txtEmergencia.text = "Consultando..."
        findViewById<TextView>(R.id.tvRecordatoriosCitas).text = "Consultando..."
        findViewById<TextView>(R.id.tvAvisosCampanias).text = "Consultando..."
        recordatorios.isEnabled = false
        campanias.isEnabled = false

        configurarListeners()
        actualizarContenedor()
        supportFragmentManager.addOnBackStackChangedListener(
            object : FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    actualizarContenedor()
                    if (supportFragmentManager.backStackEntryCount == 0) {
                        presentador.cargarConfiguracion(token)
                    }
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (::presentador.isInitialized && supportFragmentManager.backStackEntryCount == 0) {
            presentador.cargarConfiguracion(token)
        }
    }

    private fun configurarListeners() {
        val listener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (v?.id) {
                    R.id.ibRegresar -> onBackPressedDispatcher.onBackPressed()
                    R.id.clCorreoInstitucional -> abrirFragmento(AjustesCorreoFragmentC())
                    R.id.clCambiarContrasena -> abrirFragmento(AjustesContrasenaFragmentC())
                    R.id.clTelefonoEmergencia -> abrirFragmento(AjustesEmergenciaFragmentC())
                    R.id.clTerminosServicio -> startActivity(Intent(this@ConfiguracionActivityC, TerminosActivityC::class.java))
                    R.id.clPrivacidad -> startActivity(Intent(this@ConfiguracionActivityC, PrivacidadActivityC::class.java))
                    R.id.clPreguntasFrecuentes -> startActivity(Intent(this@ConfiguracionActivityC, PreguntasFrecuentesActivityC::class.java))
                    R.id.btnCerrarSesion -> cerrarSesion()
                }
            }
        }

        findViewById<View>(R.id.ibRegresar).setOnClickListener(listener)
        findViewById<View>(R.id.clCorreoInstitucional).setOnClickListener(listener)
        findViewById<View>(R.id.clCambiarContrasena).setOnClickListener(listener)
        findViewById<View>(R.id.clTelefonoEmergencia).setOnClickListener(listener)
        findViewById<View>(R.id.clTerminosServicio).setOnClickListener(listener)
        findViewById<View>(R.id.clPrivacidad).setOnClickListener(listener)
        findViewById<View>(R.id.clPreguntasFrecuentes).setOnClickListener(listener)
        findViewById<View>(R.id.btnCerrarSesion).setOnClickListener(listener)

        val notificaciones = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (!actualizandoVista && configuracionCargada) {
                    presentador.guardarNotificaciones(token, recordatorios.isChecked, campanias.isChecked)
                }
            }
        }
        recordatorios.setOnCheckedChangeListener(notificaciones)
        campanias.setOnCheckedChangeListener(notificaciones)

    }

    private fun abrirFragmento(fragmento: Fragment) {
        if (supportFragmentManager.isStateSaved || supportFragmentManager.backStackEntryCount > 0) return
        contenedor.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorAjustesC, fragmento)
            .addToBackStack(null)
            .commit()
    }

    private fun actualizarContenedor() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            contenedor.visibility = View.VISIBLE
        } else {
            contenedor.visibility = View.GONE
        }
    }

    private fun cerrarSesion() {
        val destino = Intent(this, LoginActivityC::class.java)
        destino.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(destino)
        finish()
    }

    fun mostrarConfiguracion(datos: dataPerfilConfiguracionC) {
        txtCorreo.text = if (datos.correo.isBlank()) "Sin correo registrado" else datos.correo
        txtEmergencia.text = if (datos.telefonoContacto.isBlank()) "Sin contacto registrado" else datos.telefonoContacto
        configuracionCargada = true
        actualizarNotificaciones(datos.recordatoriosCitas, datos.avisosCampanias)
        mostrarCargando(false)
    }

    private fun actualizarNotificaciones(citas: Boolean, avisos: Boolean) {
        actualizandoVista = true
        recordatorios.isChecked = citas
        campanias.isChecked = avisos
        actualizandoVista = false
        findViewById<TextView>(R.id.tvRecordatoriosCitas).text = if (citas) "Activados" else "Desactivados"
        findViewById<TextView>(R.id.tvAvisosCampanias).text = if (avisos) "Activados" else "Desactivados"
    }

    fun mostrarCargando(cargando: Boolean) {
        recordatorios.isEnabled = !cargando && configuracionCargada
        campanias.isEnabled = !cargando && configuracionCargada
    }

    fun mostrarGuardando(guardando: Boolean) {
        mostrarCargando(guardando)
    }

    fun mostrarPreferenciasGuardadas(recordatoriosGuardados: Boolean, campaniasGuardadas: Boolean) {
        actualizarNotificaciones(recordatoriosGuardados, campaniasGuardadas)
        Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
    }

    fun mostrarError(mensaje: String) {
        if (::txtEmergencia.isInitialized && !configuracionCargada) {
            txtEmergencia.text = "No disponible"
            findViewById<TextView>(R.id.tvRecordatoriosCitas).text = "No disponible"
            findViewById<TextView>(R.id.tvAvisosCampanias).text = "No disponible"
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
