package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsAjustesContrasenaPresentadorC
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AjustesContrasenaFragmentC : Fragment() {

    private lateinit var presentador: clsAjustesContrasenaPresentadorC
    private lateinit var actual: TextInputEditText
    private lateinit var nueva: TextInputEditText
    private lateinit var confirmar: TextInputEditText
    private lateinit var boton: MaterialButton

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_ajustes_contrasena_c,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        actual = view.findViewById(R.id.etContrasenaActual)
        nueva = view.findViewById(R.id.etContrasenaNueva)
        confirmar = view.findViewById(R.id.etConfirmarContrasena)
        boton = view.findViewById(R.id.btnActualizarContrasena)

        token = requireActivity().intent
            .getStringExtra("token").orEmpty()

        presentador = clsAjustesContrasenaPresentadorC(this)

        configurarListeners(view)
    }

    private fun configurarListeners(view: View) {

        view.findViewById<View>(R.id.ibRegresar)
            .setOnClickListener(
                object : View.OnClickListener {

                    override fun onClick(v: View?) {
                        parentFragmentManager.popBackStack()
                    }
                }
            )

        boton.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val contrasenaActual = actual.text.toString()
                    val contrasenaNueva = nueva.text.toString()
                    val confirmarContrasena = confirmar.text.toString()

                    presentador.actualizar(
                        token,
                        contrasenaActual,
                        contrasenaNueva,
                        confirmarContrasena
                    )
                }
            }
        )
    }

    fun mostrarCargando(cargando: Boolean) {

        if (!isAdded || view == null) {
            return
        }

        boton.isEnabled = !cargando

        if (cargando) {
            boton.text = "Actualizando..."
        } else {
            boton.text = getString(
                R.string.ajustes_contrasena_guardar
            )
        }
    }

    fun mostrarError(mensaje: String) {

        if (!isAdded || view == null) {
            return
        }

        Toast.makeText(
            requireContext(),
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }

    fun mostrarContrasenaActualizada() {

        if (!isAdded || view == null) {
            return
        }

        actual.text?.clear()
        nueva.text?.clear()
        confirmar.text?.clear()

        Toast.makeText(
            requireContext(),
            "Contraseña actualizada correctamente",
            Toast.LENGTH_LONG
        ).show()

        val destino = Intent(
            requireContext(),
            LoginActivityC::class.java
        )

        destino.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(destino)
        requireActivity().finish()
    }

    override fun onDestroyView() {

        if (::presentador.isInitialized) {
            presentador.cancelar()
        }

        super.onDestroyView()
    }
}