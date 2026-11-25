package com.example.servicios_medicos.vista

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class EnfermeraMainActivityJ : AppCompatActivity() {

    private lateinit var bottomNavEnfermera: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enfermera_main_j)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        bottomNavEnfermera = findViewById(R.id.bottomNavEnfermera)

        if (savedInstanceState == null) {
            // Descomenta la siguiente línea en cuanto creemos InicioEnfermeraFragment
            // cargarFragmento(InicioEnfermeraFragment())
        }

        configurarListeners()
    }

    private fun configurarListeners() {
        bottomNavEnfermera.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    // cargarFragmento(InicioEnfermeraFragment())
                    true
                }

                R.id.nav_citas -> {
                    // cargarFragmento(CitasEnfermeraFragment())
                    true
                }

                R.id.nav_campanias -> {
                    // cargarFragmento(campanias_jn())
                    true
                }
                else -> false
            }
        }
    }

    private fun cargarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragmentEnfermera, fragmento)
            .commit()
    }
}