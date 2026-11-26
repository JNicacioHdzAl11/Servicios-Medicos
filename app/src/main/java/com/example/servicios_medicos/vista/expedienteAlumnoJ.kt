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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.adapters.consultaAdapter
import com.example.servicios_medicos.modelo.dataConsultaJ
import com.example.servicios_medicos.modelo.dataExpedienteJN
import com.example.servicios_medicos.modelo.dataSolicitudExpedienteJ
import com.example.servicios_medicos.presentador.clsExpedienteMedicoAlumPresentadorJM
import com.google.android.material.button.MaterialButton

class expedienteAlumnoJ : Fragment(), consultaAdapter.OnConsultaClickListener {

    private lateinit var btnRegresar: ImageButton
    private lateinit var btnAjustes: ImageButton

    private lateinit var txtNombreAlumno: TextView
    private lateinit var txtMatriculaActualizacion: TextView

    private lateinit var txtTipoSangre: TextView
    private lateinit var txtAlergias: TextView
    private lateinit var txtAlergiasNota: TextView

    private lateinit var txtPadecimientos: TextView
    private lateinit var txtCirugias: TextView
    private lateinit var txtTratamiento: TextView

    private lateinit var btnVerHistorial: MaterialButton
    private lateinit var rvConsultasRecientes: RecyclerView
    private lateinit var adapterConsultas: consultaAdapter

    private lateinit var presentador: clsExpedienteMedicoAlumPresentadorJM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expediente_alumno_j, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enlazarVistas(view)
        configurarListeners()

        presentador = clsExpedienteMedicoAlumPresentadorJM(this)

        val token = requireActivity().intent.getStringExtra("token") ?: ""
        if (token.isNotEmpty()) {
            presentador.cargarDatosExpediente(token)
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

    private fun enlazarVistas(view: View) {
        btnRegresar = view.findViewById(R.id.btnRegresar)
        btnAjustes = view.findViewById(R.id.btnAjustes)

        txtNombreAlumno = view.findViewById(R.id.txtNombreAlumno)
        txtMatriculaActualizacion = view.findViewById(R.id.txtMatriculaActualizacion)

        txtTipoSangre = view.findViewById(R.id.txtTipoSangre)
        txtAlergias = view.findViewById(R.id.txtAlergias)
        txtAlergiasNota = view.findViewById(R.id.txtAlergiasNota)

        txtPadecimientos = view.findViewById(R.id.txtPadecimientos)
        txtCirugias = view.findViewById(R.id.txtCirugias)
        txtTratamiento = view.findViewById(R.id.txtTratamiento)

        btnVerHistorial = view.findViewById(R.id.btnVerHistorial)
        rvConsultasRecientes = view.findViewById(R.id.rvConsultasRecientes)
        rvConsultasRecientes.layoutManager = LinearLayoutManager(requireContext())

        adapterConsultas = consultaAdapter(emptyList(), this)
        rvConsultasRecientes.adapter = adapterConsultas
    }

    private fun configurarListeners() {
        btnRegresar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        })

        btnAjustes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(requireContext(), "Abrir configuración", Toast.LENGTH_SHORT).show()
            }
        })

        btnVerHistorial.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(requireContext(), "Abrir historial completo de consultas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun mostrarCargando(cargando: Boolean) {
        btnVerHistorial.isEnabled = !cargando
    }

    fun mostrarDatosExpediente(expediente: dataExpedienteJN, ultimaSolicitud: dataSolicitudExpedienteJ?) {
        txtNombreAlumno.text = expediente.nombreCompleto.trim()

        val textoSubtitulo = if (ultimaSolicitud != null && !ultimaSolicitud.fechaSolicitud.isNullOrBlank()) {
            "Matrícula ${expediente.matricula} · Actualizado el ${formatearFechaCorta(ultimaSolicitud.fechaSolicitud)}"
        } else {
            "Matrícula ${expediente.matricula}"
        }
        txtMatriculaActualizacion.text = textoSubtitulo

        txtTipoSangre.text = if (!expediente.tipoSangre.isNullOrBlank()) {
            expediente.tipoSangre
        } else {
            "N/D"
        }

        if (!expediente.alergias.isNullOrBlank() && !expediente.alergias.equals("Ninguna", ignoreCase = true)) {
            txtAlergias.text = expediente.alergias
            txtAlergiasNota.visibility = View.VISIBLE
        } else {
            txtAlergias.text = "Ninguna"
            txtAlergiasNota.visibility = View.GONE
        }

        txtPadecimientos.text = if (!expediente.enfermedades.isNullOrBlank()) {
            expediente.enfermedades
        } else {
            "Sin registros"
        }

        txtCirugias.text = if (!expediente.antecedentes.isNullOrBlank()) {
            expediente.antecedentes
        } else {
            "Sin antecedentes"
        }

        txtTratamiento.text = if (!expediente.motivoConsulta.isNullOrBlank()) {
            expediente.motivoConsulta
        } else {
            "Sin registro activo"
        }
    }

    fun mostrarConsultasRecientes(consultas: List<dataConsultaJ>) {
        adapterConsultas.actualizarLista(consultas)
    }

    override fun onDescargarRecetaClick(consulta: dataConsultaJ) {
        Toast.makeText(
            requireContext(),
            "Descargando receta #${consulta.idReceta}...",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onConsultaItemClick(consulta: dataConsultaJ) {
        Toast.makeText(
            requireContext(),
            "Diagnóstico: ${consulta.diagnostico ?: "Sin diagnóstico registrado"}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun formatearFechaCorta(fechaRaw: String): String {
        return try {
            val meses = mapOf(
                "01" to "ene", "02" to "feb", "03" to "mar", "04" to "abr",
                "05" to "may", "06" to "jun", "07" to "jul", "08" to "ago",
                "09" to "sep", "10" to "oct", "11" to "nov", "12" to "dic"
            )
            val soloFecha = fechaRaw.split(" ")[0]
            val partes = soloFecha.split("-")
            "${partes[2]} ${meses[partes[1]] ?: partes[1]} ${partes[0]}"
        } catch (e: Exception) {
            fechaRaw
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}