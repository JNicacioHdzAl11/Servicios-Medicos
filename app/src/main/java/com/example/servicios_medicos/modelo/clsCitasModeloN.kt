package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente

class clsCitasModeloN {

    suspend fun listarCitas(token: String): dataRespuestaN<List<dataCitaN>> {
        return RetrofitCliente.apiAlumno.listarCitas("Bearer $token")
    }

    suspend fun obtenerDetalleCita(token: String, idCita: Int): dataRespuestaN<dataCitaN> {
        return RetrofitCliente.apiAlumno.obtenerDetalleCita("Bearer $token", idCita)
    }

    suspend fun cancelarCita(
        token: String,
        idCita: Int,
        motivoCancelacion: String
    ): dataRespuestaN<dataCitaCanceladaN> {
        val solicitud = dataCancelarCitaRequestN(
            idCita = idCita,
            motivoCancelacion = motivoCancelacion
        )
        return RetrofitCliente.apiAlumno.cancelarCita("Bearer $token", solicitud)
    }

    suspend fun obtenerComprobanteIngreso(token: String, idCita: Int): dataRespuestaN<dataComprobanteQrN> {
        return RetrofitCliente.apiAlumno.obtenerComprobanteIngreso("Bearer $token", idCita)
    }
}