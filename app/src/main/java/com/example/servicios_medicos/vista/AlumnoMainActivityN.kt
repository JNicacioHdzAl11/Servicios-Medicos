package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment

class AlumnoMainActivityN : AppCompatActivity() {
    private lateinit var bnvNavegacion: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alumno_main_n)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        if (savedInstanceState == null) {
            cargarFragmento(InicioAlumnoFragmentN())
        }

        configurarListeners()
    }
    private fun configurarListeners() {
        bnvNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    cargarFragmento(InicioAlumnoFragmentN())
                    true
                }
                R.id.nav_expediente -> {
                    val intent = Intent(this, ExpedienteAlumnoActivityJM::class.java)
                    // Si necesitas pasar el token o matrícula que recibió AlumnoMainActivityN:
                    intent.putExtras(this.intent)
                    startActivity(intent)
                    true
                }
                R.id.nav_campanias -> {
                    cargarFragmento(campanias_jn())
                    true
                }
                R.id.nav_perfil -> {
                    cargarFragmento(perfilAlumnoN())
                    true
                }
                else -> false
            }
        }
    }

    private fun cargarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcvContenedor, fragmento)
            .commit()
    }
}