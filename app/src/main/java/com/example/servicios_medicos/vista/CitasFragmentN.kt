package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.adaptador.clsCitasAdaptadorN
import com.example.servicios_medicos.modelo.DiaSemanaN
import com.example.servicios_medicos.modelo.TarjetaCitaN
import com.example.servicios_medicos.presentador.clsCitasPresentadorN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class CitasFragmentN : Fragment() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var btnAjustes: ImageButton
    private lateinit var btnProximas: MaterialButton
    private lateinit var btnHistorial: MaterialButton
    private lateinit var txtMes: TextView
    private lateinit var btnSemanaAnterior: ImageButton
    private lateinit var btnSemanaSiguiente: ImageButton
    private lateinit var layDias: List<LinearLayout>
    private lateinit var txtDiasLetras: List<TextView>
    private lateinit var txtDiasNumeros: List<TextView>
    private lateinit var txtSeccionAgendadas: TextView
    private lateinit var rvCitas: RecyclerView
    private lateinit var btnSolicitarCita: MaterialButton
    private lateinit var presentador: clsCitasPresentadorN

    override fun onCreateView( // carga el diseño de la pantalla mis citas
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_citas_n, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca mis citas
        super.onViewCreated(view, savedInstanceState)

        btnRegresar = view.findViewById(R.id.ibRegresar)
        btnAjustes = view.findViewById(R.id.ibAjustes)
        btnAjustes.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val destino = Intent(requireContext(), ConfiguracionActivityC::class.java)
                destino.putExtra("token", requireActivity().intent.getStringExtra("token"))
                destino.putExtra("correo", requireActivity().intent.getStringExtra("correo"))
                startActivity(destino)
            }
        })
        btnProximas = view.findViewById(R.id.btnProximas)
        btnHistorial = view.findViewById(R.id.btnHistorial)
        txtMes = view.findViewById(R.id.tvMes)
        btnSemanaAnterior = view.findViewById(R.id.ibSemanaAnterior)
        btnSemanaSiguiente = view.findViewById(R.id.ibSemanaSiguiente)
        layDias = listOf(
            view.findViewById(R.id.llDia1),
            view.findViewById(R.id.llDia2),
            view.findViewById(R.id.llDia3),
            view.findViewById(R.id.llDia4),
            view.findViewById(R.id.llDia5),
            view.findViewById(R.id.llDia6),
            view.findViewById(R.id.llDia7)
        )
        txtDiasLetras = listOf(
            view.findViewById(R.id.tvDiaLetra1),
            view.findViewById(R.id.tvDiaLetra2),
            view.findViewById(R.id.tvDiaLetra3),
            view.findViewById(R.id.tvDiaLetra4),
            view.findViewById(R.id.tvDiaLetra5),
            view.findViewById(R.id.tvDiaLetra6),
            view.findViewById(R.id.tvDiaLetra7)
        )
        txtDiasNumeros = listOf(
            view.findViewById(R.id.tvDiaNumero1),
            view.findViewById(R.id.tvDiaNumero2),
            view.findViewById(R.id.tvDiaNumero3),
            view.findViewById(R.id.tvDiaNumero4),
            view.findViewById(R.id.tvDiaNumero5),
            view.findViewById(R.id.tvDiaNumero6),
            view.findViewById(R.id.tvDiaNumero7)
        )
        txtSeccionAgendadas = view.findViewById(R.id.tvSeccionAgendadas)
        rvCitas = view.findViewById(R.id.rvCitas)
        rvCitas.layoutManager = LinearLayoutManager(requireContext())
        rvCitas.isNestedScrollingEnabled = false
        btnSolicitarCita = view.findViewById(R.id.btnSolicitarCita)

        presentador = clsCitasPresentadorN(this)

        configurarListeners()

        val token = requireActivity().intent.getStringExtra("token")
        presentador.iniciarPantalla(token)
    }

    override fun onResume() { // vuelve a cargar las citas cada vez que se ve la pantalla
        super.onResume()
        presentador.cargarCitas()
    }

    private fun configurarListeners() { // dice que hace cada boton, pestaña y dia
        btnRegresar.setOnClickListener {
            val bnvNavegacion = requireActivity().findViewById<BottomNavigationView>(R.id.bnvNavegacion)
            bnvNavegacion.selectedItemId = R.id.nav_inicio
        }

        btnProximas.setOnClickListener {
            presentador.elegirPestania("proximas")
        }
        btnHistorial.setOnClickListener {
            presentador.elegirPestania("historial")
        }

        btnSemanaAnterior.setOnClickListener {
            presentador.semanaAnterior()
        }
        btnSemanaSiguiente.setOnClickListener {
            presentador.semanaSiguiente()
        }
        for (i in 0 until layDias.size) {
            layDias[i].setOnClickListener {
                presentador.elegirDia(i)
            }
        }


        btnSolicitarCita.setOnClickListener {
            presentador.solicitarCita()
        }
    }

    fun marcarPestania(pestania: String) { // marca la pestaña proximas o historial
        if (pestania == "historial") {
            btnHistorial.isChecked = true
        } else {
            btnProximas.isChecked = true
        }
    }

    fun mostrarTextoProximas(texto: String) { // muestra cuantas citas proximas hay en la pestaña
        btnProximas.text = texto
    }

    fun mostrarMes(texto: String) { // muestra el mes del calendario
        txtMes.text = texto
    }

    fun mostrarSemana(dias: List<DiaSemanaN>) { // muestra los 7 dias de la semana
        for (i in 0 until dias.size) {
            txtDiasLetras[i].text = dias[i].letra
            txtDiasNumeros[i].text = dias[i].numero
        }
    }

    fun marcarDia(numeroDia: Int) { // pinta de azul el dia elegido
        val colorBlanco = ContextCompat.getColor(requireContext(), R.color.color_boton_primario_texto)
        val colorPrincipal = ContextCompat.getColor(requireContext(), R.color.color_texto_principal)
        val colorSecundario = ContextCompat.getColor(requireContext(), R.color.color_texto_secundario)
        for (i in 0 until layDias.size) {
            if (i == numeroDia) {
                layDias[i].setBackgroundResource(R.drawable.bg_dia_seleccionado)
                txtDiasLetras[i].setTextColor(colorBlanco)
                txtDiasNumeros[i].setTextColor(colorBlanco)
            } else {
                layDias[i].setBackgroundResource(android.R.color.transparent)
                txtDiasLetras[i].setTextColor(colorSecundario)
                txtDiasNumeros[i].setTextColor(colorPrincipal)
            }
        }
    }

    fun mostrarTituloSeccion(texto: String) { // cambia el titulo de la lista de citas
        txtSeccionAgendadas.text = texto
    }

    fun mostrarCitas(tarjetas: List<TarjetaCitaN>) { // manda las tarjetas a la lista de citas
        rvCitas.adapter = clsCitasAdaptadorN(tarjetas, this)
    }

    fun tocarCita(posicion: Int) { // avisa al presentador que cita toco el alumno
        presentador.verDetalle(posicion)
    }

    fun abrirDetalle(token: String, idCita: Int) { // abre detalle cita con la cita elegida
        val intent = Intent(requireContext(), DetalleCitaActivityN::class.java)
        intent.putExtra("token", token)
        intent.putExtra("id_cita", idCita)
        startActivity(intent)
    }

    fun abrirAgendarCita(token: String) { // abre agendar cita
        val intent = Intent(requireContext(), AgendarCitaActivityN::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
    }
}
