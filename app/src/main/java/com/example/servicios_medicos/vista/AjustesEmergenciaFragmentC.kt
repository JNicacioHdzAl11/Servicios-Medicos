package com.example.servicios_medicos.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsAjustesEmergenciaPresentadorC
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AjustesEmergenciaFragmentC : Fragment() {

    private lateinit var presentador: clsAjustesEmergenciaPresentadorC
    private lateinit var nombre: TextInputEditText
    private lateinit var parentesco: TextInputEditText
    private lateinit var telefono: TextInputEditText
    private lateinit var boton: MaterialButton

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_ajustes_emergencia_c,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        nombre = view.findViewById(R.id.etNombreContacto)
        parentesco = view.findViewById(R.id.etParentesco)
        telefono = view.findViewById(R.id.etTelefonoContacto)
        boton = view.findViewById(R.id.btnGuardarContacto)

        token = requireActivity().intent
            .getStringExtra("token").orEmpty()

        presentador = clsAjustesEmergenciaPresentadorC(this)

        configurarListeners(view)

        presentador.consultarContacto(token)
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

                    val nombreContacto = nombre.text.toString()
                    val parentescoContacto = parentesco.text.toString()
                    val telefonoContacto = telefono.text.toString()

                    presentador.guardarContacto(
                        token,
                        nombreContacto,
                        parentescoContacto,
                        telefonoContacto
                    )
                }
            }
        )
    }

    fun mostrarContacto(
        nombreContacto: String,
        parentescoContacto: String,
        telefonoContacto: String
    ) {

        if (!isAdded || view == null) {
            return
        }

        nombre.setText(nombreContacto)
        parentesco.setText(parentescoContacto)
        telefono.setText(telefonoContacto)
    }

    fun mostrarCargando(cargando: Boolean) {

        if (!isAdded || view == null) {
            return
        }

        boton.isEnabled = !cargando

        if (cargando) {
            boton.text = "Cargando..."
        } else {
            boton.text = getString(
                R.string.ajustes_emergencia_guardar
            )
        }
    }

    fun mostrarGuardado() {

        if (!isAdded || view == null) {
            return
        }

        Toast.makeText(
            requireContext(),
            "Contacto guardado correctamente",
            Toast.LENGTH_LONG
        ).show()

        parentFragmentManager.popBackStack()
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

    override fun onDestroyView() {

        if (::presentador.isInitialized) {
            presentador.cancelar()
        }

        super.onDestroyView()
    }
}