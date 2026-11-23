package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R

class ConfiguracionActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<android.view.View>(R.id.ibRegresar).setOnClickListener { finish() }
        findViewById<android.view.View>(R.id.clTerminosServicio).setOnClickListener {
            startActivity(Intent(this, TerminosActivityC::class.java))
        }
        findViewById<android.view.View>(R.id.clPreguntasFrecuentes).setOnClickListener {
            startActivity(Intent(this, PreguntasFrecuentesActivityC::class.java))
        }
        findViewById<android.view.View>(R.id.clCambiarContrasena).setOnClickListener {
            startActivity(Intent(this, RecuperarContrasenaActivityC::class.java))
        }
        findViewById<android.view.View>(R.id.btnCerrarSesion).setOnClickListener {
            val login = Intent(this, LoginActivityC::class.java)
            login.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(login)
        }
    }
}