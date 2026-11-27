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
import com.example.servicios_medicos.adaptador.clsConsultaAdaptadorJN
import com.example.servicios_medicos.Data.dataExpedienteJN
import com.example.servicios_medicos.Data.dataSolicitudExpedienteJ
import com.example.servicios_medicos.presentador.clsExpedienteAlumPresentadorJM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.servicios_medicos.Data.dataConsultaJN

class expedienteAlumnoJ : Fragment(), clsConsultaAdaptadorJN.OnConsultaClickListener {

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

    private lateinit var rvConsultasRecientes: RecyclerView
    private lateinit var adapterConsultas: clsConsultaAdaptadorJN

    private lateinit var presentador: clsExpedienteAlumPresentadorJM

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

        presentador = clsExpedienteAlumPresentadorJM(this)

        val token = requireActivity().intent.getStringExtra("token") ?: ""
        if (token.isNotEmpty()) {
            presentador.cargarDatosExpediente(token)
        } else {
            mostrarError("No se recibió token de sesión")
        }
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

        rvConsultasRecientes = view.findViewById(R.id.rvConsultasRecientes)
        rvConsultasRecientes.layoutManager = LinearLayoutManager(requireContext())

        adapterConsultas = clsConsultaAdaptadorJN(emptyList(), this)
        rvConsultasRecientes.adapter = adapterConsultas
    }

    private fun configurarListeners() {
        btnRegresar.setOnClickListener {
            val bnvNavegacion = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
            bnvNavegacion.selectedItemId = R.id.nav_inicio
        }

        btnAjustes.setOnClickListener {
            val destino = Intent(requireContext(), ConfiguracionActivityC::class.java)
            destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
            destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
            startActivity(destino)
        }
    }

    fun mostrarCargando(cargando: Boolean) {
        // Puedes mostrar u ocultar un ProgressBar aquí si lo agregas al XML
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

    fun mostrarConsultasRecientes(consultas: List<dataConsultaJN>) {
        adapterConsultas.actualizarLista(consultas)
    }

    override fun onDescargarRecetaClick(consulta: dataConsultaJN) {
        Toast.makeText(
            requireContext(),
            "Descargando receta #${consulta.idReceta}...",
            Toast.LENGTH_SHORT
        ).show()
    }

    // AQUÍ SE ENLAZA CON LA PANTALLA DETALLE DIAGNÓSTICO
    override fun onConsultaItemClick(consulta: dataConsultaJN) {
        val intent = Intent(requireContext(), DetalleDiagnosticoJN::class.java).apply {
            putExtra("token", requireActivity().intent.getStringExtra("token"))
            putExtra("id_consulta", consulta.idConsulta)
        }
        startActivity(intent)
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