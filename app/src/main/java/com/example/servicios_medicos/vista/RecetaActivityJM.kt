package com.example.servicios_medicos.vista

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.modelo.MedicamentoRecetaJM
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsRecetaPresentadorJM
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class RecetaActivityJM : AppCompatActivity() {
    private lateinit var btnRegresar: ImageButton
    private lateinit var txtTitulo: TextView
    private lateinit var ivAvatarMedico: ShapeableImageView

    private lateinit var txtNombreReceta: TextView
    private lateinit var txtMatriculaCarreraReceta: TextView
    private lateinit var txtFechaMedicoReceta: TextView

    private lateinit var edtBuscarMedicamento: TextInputEditText
    private lateinit var rvMedicamentosReceta: RecyclerView
    private lateinit var btnAgregarMedicamento: MaterialButton

    private lateinit var edtIndicacionesMedicas: TextInputEditText
    private lateinit var txtAlertaAlergiaReceta: TextView
    private lateinit var txtNotaReceta: TextView

    private lateinit var btnCancelarReceta: MaterialButton
    private lateinit var btnGuardarBorrador: MaterialButton
    private lateinit var btnCrearEmitirReceta: MaterialButton
    private lateinit var presentador: clsRecetaPresentadorJM

    private lateinit var itemMedicamentoView: View
    private lateinit var cvMedicamentoItem: MaterialCardView
    private lateinit var tvNombreMedicamento: TextView
    private lateinit var tvPresentacionVia: TextView
    private lateinit var btnEliminarMedicamento: ImageButton
    private lateinit var tvDosis: TextView
    private lateinit var tvFrecuencia: TextView
    private lateinit var tvDuracion: TextView
    private lateinit var tvNotaMedicamento: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receta_jm)

        btnRegresar = findViewById(R.id.btnRegresar)
        txtTitulo = findViewById(R.id.txtTitulo)
        ivAvatarMedico = findViewById(R.id.ivAvatarMedico)

        txtNombreReceta = findViewById(R.id.txtNombreReceta)
        txtMatriculaCarreraReceta = findViewById(R.id.txtMatriculaCarreraReceta)
        txtFechaMedicoReceta = findViewById(R.id.txtFechaMedicoReceta)

        edtBuscarMedicamento = findViewById(R.id.edtBuscarMedicamento)
        rvMedicamentosReceta = findViewById(R.id.rvMedicamentosReceta)
        btnAgregarMedicamento = findViewById(R.id.btnAgregarMedicamento)

        edtIndicacionesMedicas = findViewById(R.id.edtIndicacionesMedicas)
        txtAlertaAlergiaReceta = findViewById(R.id.txtAlertaAlergiaReceta)
        txtNotaReceta = findViewById(R.id.txtNotaReceta)

        btnCancelarReceta = findViewById(R.id.btnCancelarReceta)
        btnGuardarBorrador = findViewById(R.id.btnGuardarBorrador)
        btnCrearEmitirReceta = findViewById(R.id.btnCrearEmitirReceta)

        configurarVistaMedicamento()
        configurarListeners()

        presentador = clsRecetaPresentadorJM(this)
        val inicial = MedicamentoRecetaJM(
            nombre = tvNombreMedicamento.text.toString(),
            presentacionVia = tvPresentacionVia.text.toString(),
            dosis = tvDosis.text.toString(),
            frecuencia = tvFrecuencia.text.toString(),
            duracion = tvDuracion.text.toString(),
            nota = tvNotaMedicamento.text.toString()
        )
        presentador.iniciarPantalla(
            intent.getStringExtra("token"),
            intent.getIntExtra("id_consulta", 0),
            inicial
        )

        intent.getStringExtra("nombre_paciente")?.takeIf { it.isNotEmpty() }?.let {
            txtNombreReceta.text = it
        }
        intent.getStringExtra("alergia")?.takeIf { it.isNotEmpty() }?.let {
            txtAlertaAlergiaReceta.text = "Alergia registrada: $it"
        }
    }

    private fun configurarVistaMedicamento() {
        itemMedicamentoView = LayoutInflater.from(this)
            .inflate(R.layout.item_medicamento_receta, rvMedicamentosReceta, false)
        rvMedicamentosReceta.addView(itemMedicamentoView)

        cvMedicamentoItem = itemMedicamentoView as MaterialCardView
        tvNombreMedicamento = itemMedicamentoView.findViewById(R.id.txtNombreMedicamentoItem)
        tvPresentacionVia = itemMedicamentoView.findViewById(R.id.txtPresentacionViaItem)
        btnEliminarMedicamento = itemMedicamentoView.findViewById(R.id.btnEliminarMedicamentoItem)
        tvDosis = itemMedicamentoView.findViewById(R.id.txtDosisItem)
        tvFrecuencia = itemMedicamentoView.findViewById(R.id.txtFrecuenciaItem)
        tvDuracion = itemMedicamentoView.findViewById(R.id.txtDuracionItem)
        tvNotaMedicamento = itemMedicamentoView.findViewById(R.id.txtNotaMedicamentoItem)
    }

    private fun configurarListeners() {
        btnRegresar.setOnClickListener { finish() }
        btnCancelarReceta.setOnClickListener { finish() }

        edtBuscarMedicamento.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presentador.filtrarMedicamento(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        btnEliminarMedicamento.setOnClickListener { presentador.eliminarUltimoMedicamento() }
        btnAgregarMedicamento.setOnClickListener {
            presentador.agregarMedicamento(edtBuscarMedicamento.text.toString())
            edtBuscarMedicamento.setText("")
        }
        btnGuardarBorrador.setOnClickListener {
            presentador.guardarReceta(edtIndicacionesMedicas.text.toString().trim(), emitir = false)
        }
        btnCrearEmitirReceta.setOnClickListener {
            presentador.guardarReceta(edtIndicacionesMedicas.text.toString().trim(), emitir = true)
        }
    }

    override fun onResume() {
        super.onResume()
        presentador.cargarReceta()
    }

    fun mostrarEncabezado(nombre: String, matriculaCarrera: String, fechaMedico: String) {
        if (nombre.isNotEmpty()) txtNombreReceta.text = nombre
        if (matriculaCarrera.isNotEmpty()) txtMatriculaCarreraReceta.text = matriculaCarrera
        if (fechaMedico.isNotEmpty()) txtFechaMedicoReceta.text = fechaMedico
    }

    fun mostrarAlertaAlergia(texto: String) {
        txtAlertaAlergiaReceta.text = texto
    }

    fun mostrarIndicaciones(indicaciones: String) {
        edtIndicacionesMedicas.setText(indicaciones)
    }

    fun mostrarMedicamentoActual(medicamento: MedicamentoRecetaJM) {
        cvMedicamentoItem.visibility = View.VISIBLE
        tvNombreMedicamento.text = medicamento.nombre
        tvPresentacionVia.text = medicamento.presentacionVia
        tvDosis.text = medicamento.dosis
        tvFrecuencia.text = medicamento.frecuencia
        tvDuracion.text = medicamento.duracion
        tvNotaMedicamento.text = medicamento.nota
    }

    fun ocultarTarjetaMedicamento() {
        cvMedicamentoItem.visibility = View.GONE
    }

    fun mostrarExito(mensaje: String, cerrarPantalla: Boolean) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        if (cerrarPantalla) finish()
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}