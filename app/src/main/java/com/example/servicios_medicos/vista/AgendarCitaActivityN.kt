package com.example.servicios_medicos.vista

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.DiaSemanaN
import com.example.servicios_medicos.presentador.clsAgendarCitaPresentadorN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class AgendarCitaActivityN : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var btnMotivosCita: List<MaterialButton>
    private lateinit var txtMes: TextView
    private lateinit var btnSemanaAnterior: ImageButton
    private lateinit var btnSemanaSiguiente: ImageButton
    private lateinit var layDias: List<LinearLayout>
    private lateinit var txtDiasLetras: List<TextView>
    private lateinit var txtDiasNumeros: List<TextView>
    private lateinit var txtFechaSeleccionada: TextView
    private lateinit var spnHorarios: Spinner
    private lateinit var txtResumenTitulo: TextView
    private lateinit var txtResumenHorario: TextView
    private lateinit var txtResumenMedico: TextView
    private lateinit var btnConfirmarCita: MaterialButton
    private lateinit var bnvNavegacion: BottomNavigationView
    private lateinit var presentador: clsAgendarCitaPresentadorN

    override fun onCreate(savedInstanceState: Bundle?) { // conecta los elementos de la pantalla y arranca agendar cita
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agendar_cita_n)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnRegresar = findViewById(R.id.ibRegresar)
        btnMotivosCita = listOf(
            findViewById(R.id.btnConsultaGeneral),
            findViewById(R.id.btnNutricion),
            findViewById(R.id.btnPsicologia),
            findViewById(R.id.btnOtroMotivo)
        )
        txtMes = findViewById(R.id.tvMes)
        btnSemanaAnterior = findViewById(R.id.ibSemanaAnterior)
        btnSemanaSiguiente = findViewById(R.id.ibSemanaSiguiente)
        layDias = listOf(
            findViewById(R.id.llDia1),
            findViewById(R.id.llDia2),
            findViewById(R.id.llDia3),
            findViewById(R.id.llDia4),
            findViewById(R.id.llDia5),
            findViewById(R.id.llDia6),
            findViewById(R.id.llDia7)
        )
        txtDiasLetras = listOf(
            findViewById(R.id.tvDiaLetra1),
            findViewById(R.id.tvDiaLetra2),
            findViewById(R.id.tvDiaLetra3),
            findViewById(R.id.tvDiaLetra4),
            findViewById(R.id.tvDiaLetra5),
            findViewById(R.id.tvDiaLetra6),
            findViewById(R.id.tvDiaLetra7)
        )
        txtDiasNumeros = listOf(
            findViewById(R.id.tvDiaNumero1),
            findViewById(R.id.tvDiaNumero2),
            findViewById(R.id.tvDiaNumero3),
            findViewById(R.id.tvDiaNumero4),
            findViewById(R.id.tvDiaNumero5),
            findViewById(R.id.tvDiaNumero6),
            findViewById(R.id.tvDiaNumero7)
        )
        txtFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada)
        spnHorarios = findViewById(R.id.spnHorarios)
        txtResumenTitulo = findViewById(R.id.tvResumenTitulo)
        txtResumenHorario = findViewById(R.id.tvResumenHorario)
        txtResumenMedico = findViewById(R.id.tvResumenMedico)
        btnConfirmarCita = findViewById(R.id.btnConfirmarCita)
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        presentador = clsAgendarCitaPresentadorN(this)

        configurarListeners()

        val token = intent.getStringExtra("token")
        val motivo = intent.getStringExtra("motivo")
        presentador.iniciarPantalla(token, motivo)
    }

    private fun configurarListeners() { // dice que hace cada boton, dia, horario y el menu de abajo
        btnRegresar.setOnClickListener {
            finish()
        }

        for (i in 0 until btnMotivosCita.size) {
            btnMotivosCita[i].setOnClickListener {
                presentador.elegirMotivo(i)
            }
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

        spnHorarios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presentador.elegirHorario(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnConfirmarCita.setOnClickListener {
            presentador.confirmarCita()
        }

        bnvNavegacion.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nav_inicio) {
                irAPantallaPrincipal("inicio")
            }
            if (item.itemId == R.id.nav_citas) {
                irAPantallaPrincipal("citas")
            }
            false
        }
        bnvNavegacion.setOnItemReselectedListener { item ->
            if (item.itemId == R.id.nav_citas) {
                irAPantallaPrincipal("citas")
            }
        }
    }

    private fun irAPantallaPrincipal(abrir: String) { // regresa a la pantalla principal y abre inicio o mis citas
        val intent = Intent(this, AlumnoMainActivityN::class.java)
        intent.putExtra("abrir", abrir)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun marcarMotivo(posicion: Int) { // marca el cuadrito del motivo que eligio el alumno
        for (i in 0 until btnMotivosCita.size) {
            if (i == posicion) {
                btnMotivosCita[i].isChecked = true
            } else {
                btnMotivosCita[i].isChecked = false
            }
        }
    }

    fun mostrarMes(texto: String) { // muestra el mes del calendario
        txtMes.text = texto
    }

    fun mostrarSemana(dias: List<DiaSemanaN>) { // muestra los 7 dias de la semana
        for (i in 0 until dias.size) {
            txtDiasLetras[i].text = dias[i].letra
            txtDiasNumeros[i].text = dias[i].numero
            layDias[i].isEnabled = dias[i].habilitado
            if (dias[i].habilitado) {
                layDias[i].alpha = 1f
            } else {
                layDias[i].alpha = 0.4f
            }
        }
    }

    fun activarFlechaAtras(activo: Boolean) { // prende o apaga la flecha para regresar de semana
        btnSemanaAnterior.isEnabled = activo
        if (activo) {
            btnSemanaAnterior.alpha = 1f
        } else {
            btnSemanaAnterior.alpha = 0.3f
        }
    }

    fun activarFlechaAdelante(activo: Boolean) { // prende o apaga la flecha para avanzar de semana
        btnSemanaSiguiente.isEnabled = activo
        if (activo) {
            btnSemanaSiguiente.alpha = 1f
        } else {
            btnSemanaSiguiente.alpha = 0.3f
        }
    }

    fun marcarDia(numeroDia: Int) { // pinta de azul el dia elegido
        val colorBlanco = ContextCompat.getColor(this, R.color.color_boton_primario_texto)
        val colorPrincipal = ContextCompat.getColor(this, R.color.color_texto_principal)
        val colorSecundario = ContextCompat.getColor(this, R.color.color_texto_secundario)
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

    fun mostrarDiaElegido(texto: String) { // muestra el texto del dia que eligio el alumno
        txtFechaSeleccionada.text = texto
    }

    fun mostrarHorarios(horasLibres: List<String>) { // llena el spinner con las horas libres del dia
        val opciones = mutableListOf<String>()
        if (horasLibres.isEmpty()) {
            opciones.add("Sin horarios disponibles")
        } else {
            opciones.add("Selecciona un horario")
        }
        for (hora in horasLibres) {
            opciones.add(hora)
        }
        val adapter = ArrayAdapter(this, R.layout.item_spinner, opciones)
        adapter.setDropDownViewResource(R.layout.item_spinner_lista)
        spnHorarios.adapter = adapter
    }

    fun mostrarResumen(titulo: String, horario: String, medico: String) { // muestra el resumen de la cita
        txtResumenTitulo.text = titulo
        txtResumenHorario.text = horario
        txtResumenMedico.text = medico
    }

    fun mostrarCargando(cargando: Boolean) { // cambia el boton confirmar mientras se guarda la cita
        btnConfirmarCita.isEnabled = !cargando
        if (cargando) {
            btnConfirmarCita.text = "Agendando..."
        } else {
            btnConfirmarCita.text = "Confirmar cita"
        }
    }

    fun mostrarCitaAgendada(mensaje: String) { // avisa que la cita se guardo y cierra la pantalla
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
