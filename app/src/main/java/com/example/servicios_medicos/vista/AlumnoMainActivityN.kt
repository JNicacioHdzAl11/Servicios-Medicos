package com.example.servicios_medicos.vista

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.content.Intent
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class AlumnoMainActivityN : AppCompatActivity() {
    private lateinit var bnvNavegacion: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {// arma la pantalla principal del alumno y abre Inicio

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alumno_main_n)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bnvNavegacion = findViewById(R.id.bnvNavegacion)
        if (savedInstanceState == null) {
            cargarFragmento(InicioAlumnoFragmentN())
        }

        configurarListeners()
    }
    private fun configurarListeners() {// cambia de pantalla segun lo que toque el usuario en el menu de abajo
        bnvNavegacion.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_inicio) {
                cargarFragmento(InicioAlumnoFragmentN())
            }
            if (item.itemId == R.id.nav_citas) {
                cargarFragmento(CitasFragmentN())
            }
            true
        }
    }
    override fun onNewIntent(intent: Intent) {// marca Inicio o Citas en el menu cuando regresa de otra pantalla
        super.onNewIntent(intent)
        val abrir = intent.getStringExtra("abrir")
        if (abrir == "inicio") {
            bnvNavegacion.selectedItemId = R.id.nav_inicio
        }
        if (abrir == "citas") {
            bnvNavegacion.selectedItemId = R.id.nav_citas
        }
    }


    private fun cargarFragmento(fragmento: Fragment) {// pone la pantalla elegida dentro del contenedor
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContenedor, fragmento)
            .commit()
    }
}