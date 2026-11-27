package com.example.servicios_medicos.presentador

import androidx.fragment.app.Fragment

class clsDoctoraMainPresentadorJM(
    private var vista: Vista?
) {

    interface Vista {
        fun cargarFragmento(fragmento: Fragment)
        fun mostrarEstadoSesion(mensaje: String)
    }

    fun alIniciarPantalla(token: String) {
        if (token.isBlank()) {
            vista?.mostrarEstadoSesion("Sesión sin token")
        } else {
            vista?.mostrarEstadoSesion("Sesión activa")
        }
    }

    fun onDestroy() {
        vista = null
    }
}