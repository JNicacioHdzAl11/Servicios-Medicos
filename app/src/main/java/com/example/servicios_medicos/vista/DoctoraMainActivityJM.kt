package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DoctoraMainActivityJM : AppCompatActivity() {

    private lateinit var bottomNavDoctora: BottomNavigationView
    private var token = ""
    private var correo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctora_main_jm)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                    return insets
                }
            }
        )

        token = intent.getStringExtra("token") ?: ""
        correo = intent.getStringExtra("correo") ?: ""
        bottomNavDoctora = findViewById(R.id.bottomNavDoctora)

        if (savedInstanceState == null) {
            cargarFragmento(InicioDoctoraFragmentJM())
            bottomNavDoctora.selectedItemId = R.id.nav_inicio
        }

        configurarListeners()
    }

    private fun configurarListeners() {
        bottomNavDoctora.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    cargarFragmento(InicioDoctoraFragmentJM())
                    true
                }
                R.id.nav_alumnos -> {
                    val destino = Intent(this, ExpedienteMedicoAlumActivityJM::class.java)
                    destino.putExtra("token", token)
                    destino.putExtra("correo", correo)
                    startActivity(destino)
                    true
                }
                R.id.nav_configuracion -> {
                    val destino = Intent(this, ConfiguracionDoctoraActivityJM::class.java)
                    destino.putExtra("token", token)
                    destino.putExtra("correo", correo)
                    startActivity(destino)
                    true
                }
                else -> false
            }
        }
    }

    private fun cargarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragmentDoctora, fragmento)
            .commit()
    }
}