package com.example.servicios_medicos.modelo

import com.example.servicios_medicos.network.RetrofitCliente

class clsAgendarCitaModeloN {

    suspend fun obtenerHorariosDisponibles(token: String, fecha: String): dataRespuestaN<dataHorariosN> {
        return RetrofitCliente.apiAlumno.obtenerHorariosDisponibles("Bearer $token", fecha)
    }

    suspend fun agendarCita(
        token: String,
        idArea: Int,
        fecha: String,
        hora: String,
        motivo: String
    ): dataRespuestaN<dataCitaAgendadaN> {
        val solicitud = dataAgendarCitaRequestN(
            idArea = idArea,
            fecha = fecha,
            hora = hora,
            motivo = motivo
        )
        return RetrofitCliente.apiAlumno.agendarCita("Bearer $token", solicitud)
    }
}