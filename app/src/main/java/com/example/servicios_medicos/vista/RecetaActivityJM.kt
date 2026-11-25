package com.example.servicios_medicos.vista

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.presentador.clsRecetaPresentadorJM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class RecetaActivityJM : AppCompatActivity() {

    private lateinit var btnVolver: ImageButton
    private lateinit var tvTitulo: TextView
    private lateinit var ivMiniAvatar: ImageView
    private lateinit var tvNombreReceta: TextView
    private lateinit var tvMatriculaCarreraReceta: TextView
    private lateinit var tvFechaMedicoReceta: TextView
    private lateinit var etBuscarMedicamento: EditText
    private lateinit var cvMedicamentoItem: MaterialCardView
    private lateinit var tvNombreMedicamento: TextView
    private lateinit var tvPresentacionVia: TextView
    private lateinit var btnEliminarMedicamento: ImageButton
    private lateinit var tvDosis: TextView
    private lateinit var tvFrecuencia: TextView
    private lateinit var tvDuracion: TextView
    private lateinit var tvNotaMedicamento: TextView
    private lateinit var btnAgregarMedicamento: Button
    private lateinit var etIndicacionesMedicas: EditText
    private lateinit var tvAlertaAlergiaReceta: TextView
    private lateinit var btnCancelarReceta: Button
    private lateinit var btnGuardarBorrador: Button
    private lateinit var btnCrearEmitirReceta: Button
    private lateinit var bnvNavegacion: BottomNavigationView

    private lateinit var presenter: clsRecetaPresentadorJM
    private var tokenSesion: String = ""
    private var idConsultaActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receta_jm)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}