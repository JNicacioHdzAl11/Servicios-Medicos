package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.Data.dataInicioDoctoraJM
import com.example.servicios_medicos.modelo.clsInicioDoctoraModeloJM
import com.example.servicios_medicos.vista.InicioDoctoraFragmentJM
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsInicioDoctoraPresentadorJM(private val vista: InicioDoctoraFragmentJM) {
    private val modelo = clsInicioDoctoraModeloJM()
    private var consulta: Call<dataInicioDoctoraJM>? = null

    fun cargarInicioDoctora(token: String) {
        if (token.isBlank()) {
            vista.mostrarError("Inicia sesión para consultar el inicio de la doctora")
            return
        }
        consulta?.cancel()
        val llamada = modelo.obtenerInicioDoctora(token)
        consulta = llamada
        llamada.enqueue(object : Callback<dataInicioDoctoraJM> {
            override fun onResponse(
                call: Call<dataInicioDoctoraJM>,
                response: Response<dataInicioDoctoraJM>
            ) {
                if (call.isCanceled) return
                val resultado = response.body()
                if (response.isSuccessful && resultado?.status == true && resultado.data != null) {
                    vista.mostrarInicioDoctora(resultado.data)
                } else {
                    vista.mostrarError(
                        if (resultado != null && resultado.message.isNotBlank()) resultado.message
                        else "No fue posible cargar el panel de la doctora (${response.code()})"
                    )
                }
            }

            override fun onFailure(call: Call<dataInicioDoctoraJM>, t: Throwable) {
                if (call.isCanceled) return
                vista.mostrarError("No se pudo conectar con el servidor")
            }
        })
    }

    fun cancelar() {
        consulta?.cancel()
    }
}