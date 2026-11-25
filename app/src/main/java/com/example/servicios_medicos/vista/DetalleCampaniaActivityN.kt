package com.example.servicios_medicos.vista

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.adapters.requisitoAdapter
import com.example.servicios_medicos.modelo.dataDetalleCampaniaJN
import com.example.servicios_medicos.presentador.clsDetalleCampaniaPresentadorN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationBarView

class DetalleCampaniaActivityN : AppCompatActivity() {

    private lateinit var btnRegresar: ImageButton

    private lateinit var txtCategoria: TextView
    private lateinit var txtNombreCampania: TextView
    private lateinit var txtLema: TextView
    private lateinit var cgEtiquetas: ChipGroup

    private lateinit var txtFechas: TextView
    private lateinit var txtHorario: TextView
    private lateinit var txtUbicacion: TextView

    private lateinit var txtDescripcion: TextView
    private lateinit var rvRequisitos: RecyclerView
    private lateinit var adapterRequisitos: requisitoAdapter

    private lateinit var txtAntesAcudir: TextView
    private lateinit var txtDespuesAplicacion: TextView
    private lateinit var btnDescargarInfografia: MaterialButton
    private lateinit var bnvNavegacion: BottomNavigationView

    private lateinit var presentador: clsDetalleCampaniaPresentadorN
    private var campaniaActual: dataDetalleCampaniaJN? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_campania_n)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                return insets
            }
        })

        btnRegresar = findViewById(R.id.btnRegresar)

        txtCategoria = findViewById(R.id.txtCategoria)
        txtNombreCampania = findViewById(R.id.txtNombreCampania)
        txtLema = findViewById(R.id.txtLema)
        cgEtiquetas = findViewById(R.id.cgEtiquetas)

        txtFechas = findViewById(R.id.txtFechas)
        txtHorario = findViewById(R.id.txtHorario)
        txtUbicacion = findViewById(R.id.txtUbicacion)

        txtDescripcion = findViewById(R.id.txtDescripcion)
        rvRequisitos = findViewById(R.id.rvRequisitos)
        rvRequisitos.layoutManager = LinearLayoutManager(this)
        adapterRequisitos = requisitoAdapter(emptyList())
        rvRequisitos.adapter = adapterRequisitos

        txtAntesAcudir = findViewById(R.id.txtAntesAcudir)
        txtDespuesAplicacion = findViewById(R.id.txtDespuesAplicacion)
        btnDescargarInfografia = findViewById(R.id.btnDescargarInfografia)
        bnvNavegacion = findViewById(R.id.bnvNavegacion)

        bnvNavegacion.selectedItemId = R.id.nav_campanias


        presentador = clsDetalleCampaniaPresentadorN(this)

        val token = intent.getStringExtra("token") ?: ""
        val idCampania = intent.getIntExtra("id_campania", 0)

        if (token.isNotEmpty() && idCampania > 0) {
            presentador.cargarDetalleCampania(token, idCampania)
        } else {
            mostrarError("No se recibieron los datos de la campaña")
        }
    }



    private fun configurarListeners() {
        btnRegresar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })




        btnDescargarInfografia.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val url = campaniaActual?.infografiaUrl
                if (!url.isNullOrBlank()) {
                    val intentUrl = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intentUrl)
                } else {
                    Toast.makeText(
                        this@DetalleCampaniaActivityN,
                        "Esta campaña no tiene infografía adjunta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        bnvNavegacion.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId == R.id.nav_campanias) {
                    finish()
                    return true
                }
                return false
            }
        })
    }

    fun mostrarCargando(cargando: Boolean) {
        btnDescargarInfografia.isEnabled = !cargando
    }

    fun mostrarDetalleCampania(detalle: dataDetalleCampaniaJN) {
        campaniaActual = detalle

        val anio = if (detalle.fechaInicio.length >= 4) detalle.fechaInicio.substring(0, 4) else "2026"
        val categoriaTexto = if (!detalle.categoria.isNullOrBlank()) detalle.categoria else "Salud preventiva"
        txtCategoria.text = "$categoriaTexto · $anio"

        txtNombreCampania.text = detalle.titulo

        if (!detalle.lema.isNullOrBlank()) {
            txtLema.text = detalle.lema
            txtLema.visibility = View.VISIBLE
        } else {
            txtLema.visibility = View.GONE
        }

        cargarEtiquetas(detalle.etiquetas)

        txtFechas.text = formatearFechasDetalle(detalle.fechaInicio, detalle.fechaFin)
        txtHorario.text = "${detalle.horario} hrs."
        txtUbicacion.text = detalle.lugar

        txtDescripcion.text = detalle.descripcion

        val listaReq = parsearRequisitos(detalle.requisitos)
        adapterRequisitos.actualizarLista(listaReq)

        txtAntesAcudir.text = if (!detalle.recomendacionAntes.isNullOrBlank()) {
            detalle.recomendacionAntes
        } else {
            "Sin recomendaciones previas registradas."
        }

        txtDespuesAplicacion.text = if (!detalle.recomendacionDespues.isNullOrBlank()) {
            detalle.recomendacionDespues
        } else {
            "Sigue las indicaciones del personal médico."
        }

        val urlInfografia = if (!detalle.infografiaUrl.isNullOrBlank()) detalle.infografiaUrl else detalle.imagenUrl
        if (detalle.infografiaUrl.isNullOrBlank()) {
            btnDescargarInfografia.visibility = View.GONE
        } else {
            btnDescargarInfografia.visibility = View.VISIBLE
        }
    }

    private fun cargarEtiquetas(etiquetas: List<String>) {
        cgEtiquetas.removeAllViews()
        if (etiquetas.isEmpty()) {
            cgEtiquetas.visibility = View.GONE
            return
        }
        cgEtiquetas.visibility = View.VISIBLE
        for (texto in etiquetas) {
            val chip = Chip(this, null, com.google.android.material.R.attr.chipStyle)
            chip.text = texto
            chip.isClickable = false
            chip.isCheckable = false
            chip.setChipBackgroundColorResource(R.color.color_fondo_azul_claro)
            chip.setTextColor(getColor(R.color.color_enlace))
            chip.chipStrokeWidth = 0f
            cgEtiquetas.addView(chip)
        }
    }

    private fun parsearRequisitos(requisitosRaw: String?): List<String> {
        if (requisitosRaw.isNullOrBlank()) {
            return listOf("No se especificaron requisitos adicionales.")
        }
        val resultado = ArrayList<String>()
        val partes = requisitosRaw.split("\n", ";", "•")
        for (parte in partes) {
            val limpio = parte.trim()
            if (limpio.isNotEmpty()) {
                resultado.add(limpio)
            }
        }
        return resultado
    }

    private fun formatearFechasDetalle(inicio: String, fin: String): String {
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
            if (diaIni == diaFin) {
                "$diaIni de $mes de $anio."
            } else {
                "$diaIni al $diaFin de $mes de $anio."
            }
        } catch (e: Exception) {
            inicio
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}