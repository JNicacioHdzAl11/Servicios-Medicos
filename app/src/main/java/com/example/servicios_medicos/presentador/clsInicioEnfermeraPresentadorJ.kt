package com.example.servicios_medicos.presentador

import com.example.servicios_medicos.modelo.clsModuloEnfermeriaModeloJ
import com.example.servicios_medicos.modelo.clsResumenEnfermeriaModeloJ
import com.example.servicios_medicos.Data.dataCitaEnfermeriaJ
import com.example.servicios_medicos.Data.dataRespuestaEnfermeriaJ
import com.example.servicios_medicos.vista.InicioEnfermera
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class clsInicioEnfermeraPresentadorJ(private val vista: InicioEnfermera) {
    private val modelo = clsModuloEnfermeriaModeloJ()
    private var solicitud: Call<*>? = null
    fun cargar(token: String) {
        cancelar()
        vista.mostrarAtenciones(clsResumenEnfermeriaModeloJ.consultar(token))
        vista.mostrarCitas("…")
        val llamada = modelo.citasPendientes(token)
        solicitud = llamada
        llamada.enqueue(object : Callback<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>> {
            override fun onResponse(call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>, response: Response<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>) {
                if (call.isCanceled) return
                val resultado = response.body()
                if (response.isSuccessful && resultado?.estado == true) {
                    var total = 0
                    for (cita in resultado.datos.orEmpty()) if (cita.fecha.startsWith(clsResumenEnfermeriaModeloJ.fechaHoy())) total++
                    vista.mostrarCitas(total.toString())
                } else vista.mostrarCitas("—")
            }
            override fun onFailure(call: Call<dataRespuestaEnfermeriaJ<List<dataCitaEnfermeriaJ>>>, t: Throwable) {
                if (!call.isCanceled) vista.mostrarCitas("—")
            }
        })
    }
    fun cancelar() { solicitud?.cancel() }
}
