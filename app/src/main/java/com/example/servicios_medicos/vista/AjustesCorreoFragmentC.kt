package com.example.servicios_medicos.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R

class AjustesCorreoFragmentC : Fragment() {

    private lateinit var txtCorreo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_ajustes_correo_c,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        txtCorreo = view.findViewById(R.id.tvCorreo)

        val correo = requireActivity().intent
            .getStringExtra("correo").orEmpty()

        if (correo.isNotBlank()) {
            txtCorreo.text = correo
        } else {
            txtCorreo.text = "Sin correo registrado"
        }

        val regresar = view.findViewById<View>(R.id.ibRegresar)

        regresar.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }
}