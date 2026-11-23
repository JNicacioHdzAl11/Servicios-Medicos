package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsCrearCuentaPresentadorC
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CrearCuentaActivityC : AppCompatActivity() {
    private lateinit var etNombre: TextInputEditText
    private lateinit var etMatricula: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText
    private lateinit var etConfirmarContrasena: TextInputEditText
    private lateinit var btnCrearCuenta: MaterialButton
    private lateinit var presentador: clsCrearCuentaPresentadorC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etNombre = findViewById(R.id.etNombre)
        etMatricula = findViewById(R.id.etMatricula)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        presentador = clsCrearCuentaPresentadorC(this)

        configurarListeners()

    }
    private fun configurarListeners() {

        btnCrearCuenta.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val nombre = etNombre.text.toString()
                    val matricula = etMatricula.text.toString()
                    val correo = etCorreo.text.toString()
                    val contrasena = etContrasena.text.toString()
                    val confirmar = etConfirmarContrasena.text.toString()

                    presentador.crearCuenta(
                        nombre,
                        matricula,
                        correo,
                        contrasena,
                        confirmar
                    )
                }
            }
        )
    }

    fun mostrarCargando(cargando: Boolean) {

        btnCrearCuenta.isEnabled = !cargando

        if (cargando) {
            btnCrearCuenta.text = "Creando cuenta..."
        } else {
            btnCrearCuenta.text = "Crear cuenta"
        }
    }

    fun mostrarCuentaCreada() {

        Toast.makeText(
            this,
            "Cuenta creada correctamente",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    fun mostrarError(mensaje: String) {

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
}