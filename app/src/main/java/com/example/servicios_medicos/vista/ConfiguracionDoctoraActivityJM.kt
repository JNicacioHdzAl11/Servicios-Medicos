package com.example.servicios_medicos.vista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
    private var cedulaActual = ""
    private var firmaActual = ""
    private var avisosCampaniasActual = false
    private var actualizandoVista = false
    private var configuracionCargada = false

    private val prefs by lazy {
        getSharedPreferences("config_doctora_jm", Context.MODE_PRIVATE)
    }

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

        cedulaActual = prefs.getString("cedula_profesional", "").orEmpty()
        firmaActual = prefs.getString("firma_digital", "").orEmpty()
        val modoOscuroInicial = prefs.getBoolean("modo_oscuro", false)

        txtCorreoDoctora.text = intent.getStringExtra("correo") ?: "Sin correo registrado"
        actualizarTextoCedulaFirma()
        actualizandoVista = true
        modoOscuro.isChecked = modoOscuroInicial
        actualizandoVista = false
        findViewById<TextView>(R.id.tvModoOscuro).text = if (modoOscuroInicial) "Activado" else "Desactivado"
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
                    R.id.clCorreoInstitucional -> mostrarDialogoCorreoInstitucional()
                    R.id.clCedulaFirma -> mostrarDialogoCedulaFirma()
                    R.id.clCambiarContrasena -> {
                        val destino = Intent(this@ConfiguracionDoctoraActivityJM, AjustesContrasenaFragmentC::class.java)
                        destino.putExtra("token", token)
                        startActivity(destino)
                    }
                    R.id.clModoOscuro -> {
                        if (modoOscuro.isEnabled) {
                            modoOscuro.isChecked = !modoOscuro.isChecked
                        }
                    }
                    R.id.clAvisosUrgencias -> {
                        if (avisosUrgencias.isEnabled) {
                            avisosUrgencias.isChecked = !avisosUrgencias.isChecked
                        }
                    }
                    R.id.clProtocolosMedicos -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, TerminosActivityC::class.java))
                    R.id.clPrivacidadPaciente -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, PrivacidadActivityC::class.java))
                    R.id.clSoporteTecnico -> startActivity(Intent(this@ConfiguracionDoctoraActivityJM, PreguntasFrecuentesActivityC::class.java))
                    R.id.btnCerrarSesion -> cerrarSesion()
                    R.id.btnNavInicio -> {
                        val destino = Intent(this@ConfiguracionDoctoraActivityJM, DoctoraMainActivityJM::class.java).apply {
                            putExtra("token", token)
                            putExtra("correo", intent.getStringExtra("correo"))
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        startActivity(destino)
                        finish()
                    }
                    R.id.btnNavExpediente -> {
                        val destino = Intent(this@ConfiguracionDoctoraActivityJM, ExpedienteMedicoAlumActivityJM::class.java).apply {
                            putExtra("token", token)
                            putExtra("correo", intent.getStringExtra("correo"))
                            putExtra("matricula", "20230351")
                        }
                        startActivity(destino)
                        finish()
                    }
                }
            }
        }

        findViewById<View>(R.id.ibRegresar).setOnClickListener(listener)
        findViewById<View>(R.id.clCorreoInstitucional).setOnClickListener(listener)
        findViewById<View>(R.id.clCedulaFirma).setOnClickListener(listener)
        findViewById<View>(R.id.clCambiarContrasena).setOnClickListener(listener)
        findViewById<View>(R.id.clModoOscuro).setOnClickListener(listener)
        findViewById<View>(R.id.clAvisosUrgencias).setOnClickListener(listener)
        findViewById<View>(R.id.clProtocolosMedicos).setOnClickListener(listener)
        findViewById<View>(R.id.clPrivacidadPaciente).setOnClickListener(listener)
        findViewById<View>(R.id.clSoporteTecnico).setOnClickListener(listener)
        findViewById<View>(R.id.btnCerrarSesion).setOnClickListener(listener)
        findViewById<View>(R.id.btnNavInicio).setOnClickListener(listener)
        findViewById<View>(R.id.btnNavExpediente).setOnClickListener(listener)

        modoOscuro.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (actualizandoVista || !configuracionCargada) return
                prefs.edit().putBoolean("modo_oscuro", isChecked).apply()
                findViewById<TextView>(R.id.tvModoOscuro).text = if (isChecked) "Activado" else "Desactivado"
                aplicarModoOscuro(isChecked)
            }
        })

        avisosUrgencias.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (actualizandoVista || !configuracionCargada) return
                presentador.guardarPreferencias(token, isChecked, avisosCampaniasActual)
            }
        })
    }

    private fun mostrarDialogoCorreoInstitucional() {
        AlertDialog.Builder(this)
            .setTitle("Correo institucional")
            .setMessage(txtCorreoDoctora.text?.toString().orEmpty())
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun mostrarDialogoCedulaFirma() {
        val contenedor = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }
        val etCedula = EditText(this).apply {
            hint = "Cédula profesional"
            setText(cedulaActual)
        }
        val etFirma = EditText(this).apply {
            hint = "Firma digital"
            setText(firmaActual)
        }
        contenedor.addView(etCedula)
        contenedor.addView(etFirma)

        AlertDialog.Builder(this)
            .setTitle("Cédula y Firma Digital")
            .setView(contenedor)
            .setPositiveButton("Guardar") { _, _ ->
                cedulaActual = etCedula.text?.toString()?.trim().orEmpty()
                firmaActual = etFirma.text?.toString()?.trim().orEmpty()
                prefs.edit()
                    .putString("cedula_profesional", cedulaActual)
                    .putString("firma_digital", firmaActual)
                    .apply()
                actualizarTextoCedulaFirma()
                Toast.makeText(this, "Cédula y firma guardadas", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarTextoCedulaFirma() {
        txtCedulaFirma.text = when {
            cedulaActual.isNotBlank() && firmaActual.isNotBlank() ->
                "Cédula Prof. $cedulaActual · Firma activa"
            cedulaActual.isNotBlank() ->
                "Cédula Prof. $cedulaActual · Sin firma"
            else ->
                "Sin cédula registrada"
        }
    }

    private fun aplicarModoOscuro(oscuro: Boolean) {
        val modo = if (oscuro) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (AppCompatDelegate.getDefaultNightMode() != modo) {
            AppCompatDelegate.setDefaultNightMode(modo)
        }
    }

    private fun cerrarSesion() {
        val destino = Intent(this, LoginActivityC::class.java)
        destino.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(destino)
        finish()
    }

    fun mostrarConfiguracion(datos: dataPerfilConfigDoctoraJM) {
        val correo = datos.correo.orEmpty()
        val modoOscuroActivo = prefs.getBoolean("modo_oscuro", false)
        val recordatoriosActivos = datos.recordatoriosCitas ?: true
        avisosCampaniasActual = datos.avisosCampanias ?: false

        txtCorreoDoctora.text = if (correo.isBlank()) "Sin correo registrado" else correo
        actualizarTextoCedulaFirma()
        configuracionCargada = true
        actualizarPreferencias(modoOscuroActivo, recordatoriosActivos)
        aplicarModoOscuro(modoOscuroActivo)
        mostrarCargando(false)
    }

    private fun actualizarPreferencias(oscuro: Boolean, recordatorios: Boolean) {
        actualizandoVista = true
        modoOscuro.isChecked = oscuro
        avisosUrgencias.isChecked = recordatorios
        actualizandoVista = false
        findViewById<TextView>(R.id.tvModoOscuro).text = if (oscuro) "Activado" else "Desactivado"
        findViewById<TextView>(R.id.tvAvisosUrgencias).text = if (recordatorios) "Activados" else "Desactivados"
    }

    fun mostrarCargando(cargando: Boolean) {
        modoOscuro.isEnabled = !cargando && configuracionCargada
        avisosUrgencias.isEnabled = !cargando && configuracionCargada
    }

    fun mostrarGuardando(guardando: Boolean) {
        mostrarCargando(guardando)
    }

    fun mostrarPreferenciasGuardadas(recordatoriosGuardados: Boolean, avisosGuardados: Boolean) {
        avisosCampaniasActual = avisosGuardados
        actualizarPreferencias(modoOscuro.isChecked, recordatoriosGuardados)
        Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
    }

    fun mostrarError(mensaje: String) {
        if (::txtCedulaFirma.isInitialized && !configuracionCargada) {
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