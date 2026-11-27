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
import com.example.servicios_medicos.Data.InicioDoctoraDataJM
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsInicioDoctoraPresentadorJM
import com.google.android.material.button.MaterialButton
import java.util.Locale

class InicioDoctoraFragmentJM : Fragment() {

    private lateinit var btnConfiguracion: ImageButton
    private lateinit var txtSaludoDoctora: TextView
    private lateinit var txtCedulaDoctora: TextView
    private lateinit var txtFechaActual: TextView
    private lateinit var txtTotalCitasHoy: TextView
    private lateinit var txtTotalAtenciones: TextView
    private lateinit var txtInicialesPaciente: TextView
    private lateinit var txtNombrePaciente: TextView
    private lateinit var txtMatriculaEdad: TextView
    private lateinit var txtMotivoConsulta: TextView
    private lateinit var txtSignosVitalesResumen: TextView
    private lateinit var btnVerExpedienteAlumno: MaterialButton
    private lateinit var txtAbrirConfiguracion: TextView
    private lateinit var txtProxima1: TextView
    private lateinit var txtProxima2: TextView

    private lateinit var presentador: clsInicioDoctoraPresentadorJM
    private var token = ""
    private var correo = ""
    private var matriculaEspera = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio_doctora_jm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnConfiguracion = view.findViewById(R.id.btnConfiguracion)
        txtSaludoDoctora = view.findViewById(R.id.txtSaludoDoctora)
        txtCedulaDoctora = view.findViewById(R.id.txtCedulaDoctora)
        txtFechaActual = view.findViewById(R.id.txtFechaActual)
        txtTotalCitasHoy = view.findViewById(R.id.txtTotalCitasHoy)
        txtTotalAtenciones = view.findViewById(R.id.txtTotalAtenciones)
        txtInicialesPaciente = view.findViewById(R.id.txtInicialesPaciente)
        txtNombrePaciente = view.findViewById(R.id.txtNombrePaciente)
        txtMatriculaEdad = view.findViewById(R.id.txtMatriculaEdad)
        txtMotivoConsulta = view.findViewById(R.id.txtMotivoConsulta)
        txtSignosVitalesResumen = view.findViewById(R.id.txtSignosVitalesResumen)
        btnVerExpedienteAlumno = view.findViewById(R.id.btnVerExpedienteAlumno)
        txtAbrirConfiguracion = view.findViewById(R.id.txtAbrirConfiguracion)
        txtProxima1 = view.findViewById(R.id.txtProxima1)
        txtProxima2 = view.findViewById(R.id.txtProxima2)

        token = requireActivity().intent.getStringExtra("token") ?: ""
        correo = requireActivity().intent.getStringExtra("correo") ?: ""

        presentador = clsInicioDoctoraPresentadorJM(this)
        configurarListeners()
    }

    private fun configurarListeners() {
        val abrirConfig = View.OnClickListener {
            val destino = Intent(requireContext(), ConfiguracionDoctoraActivityJM::class.java)
            destino.putExtra("token", token)
            destino.putExtra("correo", correo)
            startActivity(destino)
        }
        btnConfiguracion.setOnClickListener(abrirConfig)
        txtAbrirConfiguracion.setOnClickListener(abrirConfig)

        btnVerExpedienteAlumno.setOnClickListener {
            val destino = Intent(requireContext(), ExpedienteAlumnoActivityJM::class.java)
            destino.putExtra("token", token)
            destino.putExtra("matricula", matriculaEspera)
            startActivity(destino)
        }
    }

    override fun onResume() {
        super.onResume()
        presentador.cargarInicioDoctora(token)
    }

    override fun onDestroyView() {
        presentador.cancelar()
        super.onDestroyView()
    }

    fun mostrarInicioDoctora(datos: InicioDoctoraDataJM) {
        if (datos.matriculaEspera.isNotEmpty()) matriculaEspera = datos.matriculaEspera
        txtSaludoDoctora.text = "Buenos días, ${datos.nombreCompleto}"
        txtCedulaDoctora.text = "Cédula profesional ${datos.cedula}"
        txtFechaActual.text = datos.fechaSesion
        txtTotalCitasHoy.text = String.format(Locale.US, "%02d", datos.citasHoy)
        txtTotalAtenciones.text = String.format(Locale.US, "%02d", datos.consultasRealizadas)

        val partes = datos.nombreEspera.trim().split(" ").filter { it.isNotEmpty() }
        txtInicialesPaciente.text = when {
            partes.size >= 2 -> (partes[0].take(1) + partes[1].take(1)).uppercase()
            partes.size == 1 -> partes[0].take(2).uppercase()
            else -> "AL"
        }
        txtNombrePaciente.text = datos.nombreEspera
        txtMatriculaEdad.text = datos.detalleEspera
        txtMotivoConsulta.text = datos.motivoEspera
        txtSignosVitalesResumen.text = datos.signosEspera
        txtProxima1.text = "${datos.proxima1} · ${datos.hora1}"
        txtProxima2.text = "${datos.proxima2} · ${datos.hora_2}"
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}