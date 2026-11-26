package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class EnfermeraMainActivityJ : AppCompatActivity() {
    private lateinit var menu: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("token").isNullOrBlank()) { finish(); return }
        enableEdgeToEdge()
        setContentView(R.layout.activity_enfermera_main_j)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
                return insets
            }
        })
        menu = findViewById(R.id.bottomNavEnfermera)
        menu.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navInicio -> mostrarInicio()
                    R.id.navAlumnos -> abrirAlumnos(false)
                    else -> return false
                }
                return item.itemId == R.id.navInicio
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (menu.selectedItemId != R.id.navInicio) menu.selectedItemId = R.id.navInicio
                else finish()
            }
        })
        if (savedInstanceState == null) mostrarInicio()
    }
    private fun mostrarInicio() {
        supportFragmentManager.beginTransaction().replace(R.id.navHostFragmentEnfermera, InicioEnfermera()).commit()
    }
    fun abrirAlumnos(conCita: Boolean) {
        val destino = Intent(this, BuscarAlumnoActivityJ::class.java)
        destino.putExtras(intent)
        destino.putExtra("solo_citas", conCita)
        startActivity(destino)
    }
    fun abrirConfiguracion() {
        val destino = Intent(this, ConfiguracionActivityC::class.java)
        destino.putExtras(intent)
        startActivity(destino)
    }
}
