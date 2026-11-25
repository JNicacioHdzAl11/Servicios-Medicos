package com.example.servicios_medicos.vista

import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import android.view.MenuItem

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.*
import com.example.servicios_medicos.presentador.*
import com.google.android.material.button.MaterialButton
import android.text.Editable
import android.text.TextWatcher
import java.text.Normalizer
import java.util.Locale
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.adaptador.clsAlumnosEnfermeriaAdaptadorJ

class BuscarAlumnoActivityJ : AppCompatActivity() {
    private var token = ""
    private var correo = ""

    private fun preparar(layout: Int): Boolean {
        enableEdgeToEdge()
        setContentView(layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(barras.left, barras.top, barras.right, barras.bottom)
                    return insets
                }
            })
        token = intent.getStringExtra("token").orEmpty()
        correo = intent.getStringExtra("correo").orEmpty()
        if (token.isBlank()) {
            mostrarError("Necesitas iniciar sesión como enfermera")
            finish()
            return false
        }
        return true
    }

    private fun destino(clase: Class<*>): Intent {
        val salida = Intent(this, clase)
        salida.putExtra("token", token)
        salida.putExtra("correo", correo)
        return salida
    }

    private fun conectarAjustes() {
        findViewById<View>(R.id.btnAjustes).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(destino(ConfiguracionActivityC::class.java))
            }
        })
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    private lateinit var presentador: clsAlumnosEnfermeriaPresentadorJ
    private lateinit var adaptador: clsAlumnosEnfermeriaAdaptadorJ
    private lateinit var buscar: EditText
    private lateinit var tipoBusqueda: Spinner
    private var alumnos: List<dataAlumnoEnfermeriaJ> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!preparar(R.layout.activity_buscar_alumno)) return
        presentador = clsAlumnosEnfermeriaPresentadorJ(this)
        adaptador = clsAlumnosEnfermeriaAdaptadorJ(this)
        findViewById<RecyclerView>(R.id.rvAlumnos).adapter = adaptador
        buscar = findViewById(R.id.etBuscarAlumno)
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { filtrar(s.toString()) }
            override fun afterTextChanged(s: Editable?) { }
        })
        configurarSpinner()
        conectarAjustes()
        conectarNavegacion()
        findViewById<View>(R.id.txtAlumnosRecientes).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) { if (!cargando) presentador.cargar(token) }
        })
        presentador.cargar(token)
    }
    private fun configurarSpinner() {
        tipoBusqueda = findViewById(R.id.spTipoBusquedaJ)

        val opciones = ArrayAdapter.createFromResource(
            this,
            R.array.buscar_alumno_opciones_j,
            android.R.layout.simple_spinner_item
        )
        opciones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoBusqueda.adapter = opciones

        tipoBusqueda.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!cargando) filtrar(buscar.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun normalizar(texto: String): String = Normalizer.normalize(texto, Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "").lowercase(Locale.ROOT)

    private fun filtrar(texto: String) {
        if (!::tipoBusqueda.isInitialized || cargando) return

        val filtro = normalizar(texto.trim())
        val soloConCita = tipoBusqueda.selectedItemPosition == 1
        val encontrados = ArrayList<dataAlumnoEnfermeriaJ>()
        val contador = findViewById<TextView>(R.id.txtAlumnosRecientes)
        var total = 0

        for (alumno in alumnos) {
            if (soloConCita && alumno.tieneCita == null) {
                adaptador.actualizar(emptyList())
                contador.setText(R.string.buscar_alumno_filtro_no_disponible_j)
                return
            }
            if (soloConCita && alumno.tieneCita != true) continue

            total++
            if (filtro.isEmpty() || normalizar(alumno.nombre).contains(filtro) || alumno.matricula.contains(filtro)) {
                encontrados.add(alumno)
            }
        }

        adaptador.actualizar(encontrados)
        contador.text = getString(R.string.buscar_alumno_contador_j, encontrados.size, total)
    }

    fun mostrarAlumnos(datos: List<dataAlumnoEnfermeriaJ>) {
        alumnos = datos
        filtrar(buscar.text.toString())
    }

    fun mostrarCargando(cargando: Boolean) {
        this.cargando = cargando
        buscar.isEnabled = !cargando
        tipoBusqueda.isEnabled = !cargando
        if (cargando) {
            findViewById<TextView>(R.id.txtAlumnosRecientes).setText(R.string.buscar_alumno_consultando_j)
        } else {
            filtrar(buscar.text.toString())
        }
    }

    private var cargando = false

    private fun conectarNavegacion() {
        val menu = findViewById<BottomNavigationView>(R.id.bnvNavegacion)
        menu.menu.findItem(R.id.nav_campanias)?.isVisible = false
        menu.selectedItemId = R.id.nav_alumnos
        menu.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId == R.id.nav_inicio) finish()
                return item.itemId == R.id.nav_alumnos
            }
        })
    }

    fun seleccionarAlumno(alumno: dataAlumnoEnfermeriaJ) {
        if (!cargando) presentador.consultarCitas(token, alumno)
    }

    fun mostrarOpcionesAtencion(alumno: dataAlumnoEnfermeriaJ, citas: List<dataCitaEnfermeriaJ>) {
        if (isFinishing || isDestroyed) return
        val opciones = ArrayList<String>()
        for (cita in citas) {
            opciones.add(cita.fecha + " · " + cita.hora + " · " + cita.motivo)
        }
        opciones.add("Registrar signos sin cita")
        val listener = object : android.content.DialogInterface.OnClickListener {
            override fun onClick(dialog: android.content.DialogInterface?, opcion: Int) {
                val salida = destino(TomaSignosActivityJ::class.java)
                salida.putExtra("matricula", alumno.matricula)
                salida.putExtra("nombre", alumno.nombre)
                salida.putExtra("academico", alumno.informacionAcademica())
                if (opcion < citas.size) salida.putExtra("id_cita", citas[opcion].idCita)
                startActivity(salida)
            }
        }
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(alumno.nombre)
            .setItems(opciones.toTypedArray(), listener)
            .setNegativeButton("Cancelar", null)
            .show()
    }
    override fun onDestroy() { if (::presentador.isInitialized) presentador.cancelar(); super.onDestroy() }
}
