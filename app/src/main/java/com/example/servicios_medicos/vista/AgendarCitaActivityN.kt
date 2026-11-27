package com.example.servicios_medicos.vista

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.DiaSemanaN
import com.example.servicios_medicos.presentador.clsAgendarCitaPresentadorN
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class AgendarCitaActivityN : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var btnMotivosCita: List<MaterialButton>
    private lateinit var txtMes: TextView
    private lateinit var btnSemanaAnterior: ImageButton
    private lateinit var btnSemanaSiguiente: ImageButton
    private lateinit var layDias: List<LinearLayout>
    private lateinit var txtDiasNumeros: List<TextView>
    private lateinit var txtFechaSeleccionada: TextView
    private lateinit var spnHorarios: Spinner
    private lateinit var txtResumenTitulo: TextView
    private lateinit var txtResumenHorario: TextView
    private lateinit var txtResumenMedico: TextView
    private lateinit var btnConfirmarCita: MaterialButton
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

        presentador = clsAgendarCitaPresentadorN(this)

        configurarListeners()

        val token = intent.getStringExtra("token")
        val motivo = intent.getStringExtra("motivo")
        presentador.iniciarPantalla(token, motivo)
    }

    private fun configurarListeners() { // dice que hace cada boton, dia y horario
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
    }

    fun marcarMotivo(posicion: Int) { // marca el cuadrito del motivo que eligio el alumno
        for (i in 0 until btnMotivosCita.size) {
            btnMotivosCita[i].isChecked = i == posicion
        }
    }

    fun mostrarSemana(mes: String, dias: List<DiaSemanaN>) { // muestra el mes y el numero de los 7 dias, apaga los que ya pasaron
        txtMes.text = mes
        for (i in 0 until dias.size) {
            txtDiasNumeros[i].text = dias[i].numero
            layDias[i].isEnabled = dias[i].habilitado
        }
    }

    fun activarFlechas(atras: Boolean, adelante: Boolean) { // prende o apaga las flechas de la semana
        btnSemanaAnterior.isEnabled = atras
        btnSemanaSiguiente.isEnabled = adelante
    }

    fun marcarDia(numeroDia: Int) { // marca el dia elegido y el xml lo pinta de azul
        for (i in 0 until layDias.size) {
            layDias[i].isSelected = i == numeroDia
        }
    }

    fun mostrarDiaElegido(texto: String) { // muestra el texto del dia que eligio el alumno
        txtFechaSeleccionada.text = texto
    }

    fun mostrarHorarios(opciones: List<String>) { // llena el spinner con los horarios del dia
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
            btnConfirmarCita.text = getString(R.string.agendar_btn_confirmar)
        }
    }

    fun mostrarCitaAgendada(mensaje: String) { // avisa que la cita se guardo y cierra la pantalla
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        finish()
    }

    fun mostrarError(mensaje: String) { // muestra un mensaje cuando algo sale mal
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    fun mostrarMensaje(mensaje: String) { // muestra un aviso al alumno abajo de la pantalla
        Snackbar.make(findViewById(R.id.main), mensaje, Snackbar.LENGTH_LONG).show()
    }
    fun mostrarBotonConfirmar(texto: String, activo: Boolean) { // cambia el texto del boton confirmar y lo prende o apaga
        btnConfirmarCita.text = texto
        btnConfirmarCita.isEnabled = activo
    }
}
