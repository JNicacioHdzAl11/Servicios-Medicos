package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.Data.PerfilData
import com.example.servicios_medicos.presentador.clsPerfilPresentadorJM
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomnavigation.BottomNavigationView

class perfilAlumnoN : Fragment() {

    private lateinit var btnRegresar: ImageButton
    private lateinit var btnAjustes: ImageButton
    private lateinit var txtNombreAlumno: TextView
    private lateinit var txtMatricula: TextView
    private lateinit var txtTipoCuenta: TextView
    private lateinit var txtCarrera: TextView
    private lateinit var txtGrupo: TextView
    private lateinit var txtTurno: TextView
    private lateinit var txtSemestre: TextView
    private lateinit var btnEditarDatos: MaterialButton
    private lateinit var txtEdad: TextView
    private lateinit var txtTelefono: TextView
    private lateinit var txtCorreo: TextView
    private lateinit var txtContactoNombre: TextView
    private lateinit var txtContactoTelefono: TextView
    private lateinit var btnCerrarSesion: MaterialButton

    private lateinit var presentador: clsPerfilPresentadorJM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil_alumno_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegresar = view.findViewById(R.id.btnRegresar)
        btnAjustes = view.findViewById(R.id.btnAjustes)
        txtNombreAlumno = view.findViewById(R.id.txtNombreAlumno)
        txtMatricula = view.findViewById(R.id.txtMatricula)
        txtTipoCuenta = view.findViewById(R.id.txtTipoCuenta)
        txtCarrera = view.findViewById(R.id.txtCarrera)
        txtGrupo = view.findViewById(R.id.txtGrupo)
        txtTurno = view.findViewById(R.id.txtTurno)
        txtSemestre = view.findViewById(R.id.txtSemestre)
        btnEditarDatos = view.findViewById(R.id.btnEditarDatos)
        txtEdad = view.findViewById(R.id.txtEdad)
        txtTelefono = view.findViewById(R.id.txtTelefono)
        txtCorreo = view.findViewById(R.id.txtCorreo)
        txtContactoNombre = view.findViewById(R.id.txtContactoNombre)
        txtContactoTelefono = view.findViewById(R.id.txtContactoTelefono)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)

        presentador = clsPerfilPresentadorJM(this)

        configurarListeners()

        val token = requireActivity().intent.getStringExtra("token") ?: ""
        val matriculaLogin = requireActivity().intent.getStringExtra("identificador") ?: ""

        if (matriculaLogin.isNotEmpty()) {
            txtMatricula.text = "Matrícula $matriculaLogin"
        }
        txtTipoCuenta.text = "Alumno · Cuenta institucional"

        if (token.isNotEmpty()) {
            presentador.cargarPerfil(token)
        } else {
            mostrarError("No se recibió token de sesión")
        }
        btnAjustes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val destino = Intent(requireContext(), ConfiguracionActivityC::class.java)
                destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
                destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
                startActivity(destino)
            }
        })
    }

    private fun configurarListeners() {
        btnRegresar.setOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack()
            } else {
                val bnvNavegacion = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
                bnvNavegacion?.selectedItemId = R.id.nav_inicio
            }
        }

        btnAjustes.setOnClickListener {
            val destino = Intent(requireContext(), ConfiguracionActivityC::class.java)
            destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
            destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
            startActivity(destino)
        }

        btnEditarDatos.setOnClickListener {
            // Pantalla o diálogo para editar teléfono / contacto de emergencia
        }

        btnCerrarSesion.setOnClickListener {
            val destino = Intent(requireContext(), LoginActivityC::class.java)
            destino.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(destino)
            requireActivity().finish()
        }
    }

    fun mostrarCargando(cargando: Boolean) {
        btnEditarDatos.isEnabled = !cargando
        btnCerrarSesion.isEnabled = !cargando
    }

    fun mostrarDatosPerfil(perfil: PerfilData) {
        txtNombreAlumno.text = perfil.nombreCompleto
        txtMatricula.text = "Matrícula ${perfil.matricula}"
        txtTipoCuenta.text = "Alumno · Cuenta institucional"

        txtCarrera.text = perfil.area
        txtGrupo.text = "${perfil.cuatrimestre}${perfil.grupo}"
        txtTurno.text = "Matutino"
        txtSemestre.text = "${perfil.cuatrimestre}° Cuatrimestre"

        txtEdad.text = if (perfil.edad.isNotEmpty()) "${perfil.edad} años" else "-"
        txtTelefono.text = perfil.telefono
        txtCorreo.text = perfil.correo

        val contacto = perfil.contactoEmergencia
        if (contacto != null && contacto.nombre.isNotEmpty()) {
            txtContactoNombre.text = "${contacto.nombre} · ${contacto.parentesco}"
            txtContactoTelefono.text = contacto.telefono
        } else {
            txtContactoNombre.text = "No registrado"
            txtContactoTelefono.text = "-"
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}