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
import com.example.servicios_medicos.adaptador.clsCampaniaAdaptadorJN
import com.example.servicios_medicos.Data.dataCampaniaJN
import com.example.servicios_medicos.presentador.clsCampaniasPresentadorN
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class campanias_jn : Fragment(), clsCampaniaAdaptadorJN.OnCampaniaClickListener {

    private lateinit var btnRegresar: ImageButton

    private lateinit var cardVacunacion: MaterialCardView
    private lateinit var txtNombreCampaniaDestacada: TextView
    private lateinit var txtTipoCampaniaDestacada: TextView
    private lateinit var txtDescripcionCampaniaDestacada: TextView
    private lateinit var txtFechaCampaniaDestacada: TextView
    private lateinit var txtLugarCampaniaDestacada: TextView
    private lateinit var btnVerInformacionDestacada: MaterialButton

    private lateinit var rvCampanias: RecyclerView
    private lateinit var adapter: clsCampaniaAdaptadorJN

    private lateinit var presentador: clsCampaniasPresentadorN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_campanias_jn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegresar = view.findViewById(R.id.btnRegresar)

        cardVacunacion = view.findViewById(R.id.cardVacunacion)
        txtNombreCampaniaDestacada = view.findViewById(R.id.txtNombreCampaniaDestacada)
        txtTipoCampaniaDestacada = view.findViewById(R.id.txtTipoCampaniaDestacada)
        txtDescripcionCampaniaDestacada = view.findViewById(R.id.txtDescripcionCampaniaDestacada)
        txtFechaCampaniaDestacada = view.findViewById(R.id.txtFechaCampaniaDestacada)
        txtLugarCampaniaDestacada = view.findViewById(R.id.txtLugarCampaniaDestacada)
        btnVerInformacionDestacada = view.findViewById(R.id.btnVerInformacionDestacada)

        rvCampanias = view.findViewById(R.id.rvCampanias)
        rvCampanias.layoutManager = LinearLayoutManager(requireContext())

        adapter = clsCampaniaAdaptadorJN(emptyList(), this)
        rvCampanias.adapter = adapter

        presentador = clsCampaniasPresentadorN(this)

        configurarListeners()

        val token = requireActivity().intent.getStringExtra("token") ?: ""

        if (token.isNotEmpty()) {
            presentador.cargarCampanias(token)
        } else {
            mostrarError("No se recibió token de sesión")
        }
    }

    private fun configurarListeners() {
        btnRegresar.setOnClickListener {
            val bnvNavegacion = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
            bnvNavegacion.selectedItemId = R.id.nav_inicio
        }
    }

    override fun onVerInformacionClick(campania: dataCampaniaJN) {
        abrirDetalleCampania(campania)
    }

    fun mostrarCargando(cargando: Boolean) {
        btnVerInformacionDestacada.isEnabled = !cargando
    }

    fun mostrarCampanias(destacada: dataCampaniaJN?, listaCampanias: List<dataCampaniaJN>) {
        if (destacada != null) {
            cardVacunacion.visibility = View.VISIBLE
            txtNombreCampaniaDestacada.text = destacada.titulo
            txtTipoCampaniaDestacada.text = "Jornada preventiva"
            txtDescripcionCampaniaDestacada.text = destacada.descripcion
            txtFechaCampaniaDestacada.text = formatearFechaLarga(destacada.fechaInicio, destacada.fechaFin)
            txtLugarCampaniaDestacada.text = "${destacada.horario} · ${destacada.lugar}"

            btnVerInformacionDestacada.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    abrirDetalleCampania(destacada)
                }
            })
        } else {
            cardVacunacion.visibility = View.GONE
        }

        adapter.actualizarLista(listaCampanias)
    }

    private fun abrirDetalleCampania(campania: dataCampaniaJN) {
        val token = requireActivity().intent.getStringExtra("token") ?: ""
        val intent = Intent(requireContext(), DetalleCampaniaActivityN::class.java)
        intent.putExtra("token", token)
        intent.putExtra("id_campania", campania.idCampania)
        startActivity(intent)
    }

    private fun formatearFechaLarga(inicio: String, fin: String): String {
        return try {
            val meses = mapOf(
                "01" to "enero", "02" to "febrero", "03" to "marzo", "04" to "abril",
                "05" to "mayo", "06" to "junio", "07" to "julio", "08" to "agosto",
                "09" to "septiembre", "10" to "octubre", "11" to "noviembre", "12" to "diciembre"
            )
            val pIni = inicio.split("-")
            val pFin = fin.split("-")
            val diaIni = pIni[2].toInt()
            val diaFin = pFin[2].toInt()
            val mes = meses[pFin[1]] ?: pFin[1]
            val anio = pFin[0]
            if (diaIni == diaFin) "$diaIni de $mes de $anio" else "$diaIni–$diaFin de $mes de $anio"
        } catch (e: Exception) {
            inicio
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}