package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.dataUsuarioC
import com.example.servicios_medicos.presentador.clsLoginPresentadorC
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText


class LoginActivityC : AppCompatActivity() {

    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText
    private lateinit var btnIniciarSesion: MaterialButton
    private lateinit var presentador: clsLoginPresentadorC

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_c)

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

        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        presentador = clsLoginPresentadorC(this)

        configurarListeners()

        findViewById<View>(R.id.tvCrearCuenta)
            .setOnClickListener(
                object : View.OnClickListener {

                    override fun onClick(v: View?) {

                        val intent = Intent(
                            this@LoginActivityC,
                            CrearCuentaActivityC::class.java
                        )

                        startActivity(intent)
                    }
                }
            )

        findViewById<View>(R.id.tvOlvidasteContrasena)
            .setOnClickListener(
                object : View.OnClickListener {

                    override fun onClick(v: View?) {

                        val intent = Intent(
                            this@LoginActivityC,
                            RecuperarContrasenaActivityC::class.java
                        )

                        startActivity(intent)
                    }
                }
            )
    }

    private fun configurarListeners() {

        btnIniciarSesion.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val correo = etCorreo.text.toString()
                    val contrasena = etContrasena.text.toString()

                    presentador.iniciarSesion(
                        correo,
                        contrasena
                    )
                }
            }
        )
    }

    fun mostrarCargando(cargando: Boolean) {

        btnIniciarSesion.isEnabled = !cargando

        if (cargando) {

            btnIniciarSesion.text = "Validando..."

        } else {

            btnIniciarSesion.text = "Iniciar sesión"
        }
    }

    fun mostrarInicioExitoso(usuario: dataUsuarioC) {

        val rol = usuario.rol

        if (rol.equals("alumno", ignoreCase = true)) {

            val intent = Intent(
                this,
                AlumnoMainActivityN::class.java
            )

            intent.putExtra("id_usuario", usuario.idUsuario)
            intent.putExtra("identificador", usuario.identificador)
            intent.putExtra("correo", usuario.correo)
            intent.putExtra("token", usuario.token)

            startActivity(intent)
            finish()

        } else if (rol.equals("enfermera", ignoreCase = true)) {

            val intent = Intent(
                this,
                BuscarAlumnoActivityJ::class.java
            )

            intent.putExtra("id_usuario", usuario.idUsuario)
            intent.putExtra("identificador", usuario.identificador)
            intent.putExtra("correo", usuario.correo)
            intent.putExtra("token", usuario.token)

            startActivity(intent)
            finish()

        } else if (rol.equals("doctora", ignoreCase = true)) {

            val intent = Intent(
                this,
                DoctoraMainActivityJM::class.java
            )

            intent.putExtra("id_usuario", usuario.idUsuario)
            intent.putExtra("identificador", usuario.identificador)
            intent.putExtra("correo", usuario.correo)
            intent.putExtra("token", usuario.token)

            startActivity(intent)
            finish()

        }else if (rol.equals("doctora", ignoreCase = true)) {

            val intent = Intent(
                this,
                DoctoraMainActivityJM::class.java
            )

            intent.putExtra("id_usuario", usuario.idUsuario)
            intent.putExtra("identificador", usuario.identificador)
            intent.putExtra("correo", usuario.correo)
            intent.putExtra("token", usuario.token)

            startActivity(intent)
            finish()

        } else {

            Toast.makeText(
                this,
                "Tu rol es: " + rol,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun mostrarError(mensaje: String) {

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
}