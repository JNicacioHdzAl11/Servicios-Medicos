package com.example.servicios_medicos.vista

import android.content.Intent
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.MenuItem
import android.widget.Toast
import android.widget.TextView
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.Data.dataAlumnoEnfermeriaJ
import com.example.servicios_medicos.Data.dataCitaEnfermeriaJ
import com.example.servicios_medicos.presentador.clsAlumnosEnfermeriaPresentadorJ
import com.example.servicios_medicos.adaptador.clsAlumnosEnfermeriaAdaptadorJ
import java.text.Normalizer
import java.util.Locale

class BuscarAlumnoActivityJ : AppCompatActivity() {

    private lateinit var presentador: clsAlumnosEnfermeriaPresentadorJ
    private lateinit var adaptador: clsAlumnosEnfermeriaAdaptadorJ

    private lateinit var etBuscarAlumno: EditText
    private lateinit var spTipoBusqueda: Spinner
    private lateinit var rvAlumnos: RecyclerView
    private lateinit var txtAlumnosRecientes: TextView

    private var alumnos: List<dataAlumnoEnfermeriaJ> = emptyList()

    private var token = ""
    private var correo = ""
    private var cargando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscar_alumno)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            object : OnApplyWindowInsetsListener {
                override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    return insets
                }
            }
        )

        token = intent.getStringExtra("token").orEmpty()
        correo = intent.getStringExtra("correo").orEmpty()

        if (token.isBlank()) {
            mostrarError("Necesitas iniciar sesión como enfermera")
            finish()
            return
        }

        etBuscarAlumno = findViewById(R.id.etBuscarAlumno)
        spTipoBusqueda = findViewById(R.id.spTipoBusquedaJ)
        rvAlumnos = findViewById(R.id.rvAlumnos)
        txtAlumnosRecientes = findViewById(R.id.txtAlumnosRecientes)

        presentador = clsAlumnosEnfermeriaPresentadorJ(this)
        adaptador = clsAlumnosEnfermeriaAdaptadorJ(this)

        rvAlumnos.adapter = adaptador

        configurarSpinner()
        if (intent.getBooleanExtra("solo_citas", false)) spTipoBusqueda.setSelection(1)
        configurarListeners()
        configurarNavegacion()

        presentador.cargar(token)
    }

    private fun configurarListeners() {

        etBuscarAlumno.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    filtrar(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )

        spTipoBusqueda.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    filtrar(etBuscarAlumno.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        findViewById<View>(R.id.btnAjustes).setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val intent = Intent(
                        this@BuscarAlumnoActivityJ,
                        ConfiguracionActivityC::class.java
                    )

                    intent.putExtra("token", token)
                    intent.putExtra("correo", correo)

                    startActivity(intent)
                }
            }
        )

        txtAlumnosRecientes.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    if (!cargando) {
                        presentador.cargar(token)
                    }
                }
            }
        )
    }

    private fun configurarSpinner() {

        val opciones = ArrayAdapter.createFromResource(
            this,
            R.array.buscar_alumno_opciones_j,
            android.R.layout.simple_spinner_item
        )

        opciones.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spTipoBusqueda.adapter = opciones
    }

    private fun filtrar(texto: String) {

        if (cargando) {
            return
        }

        val busqueda = normalizar(texto.trim())

        val soloConCita = spTipoBusqueda.selectedItemPosition == 1

        val encontrados = ArrayList<dataAlumnoEnfermeriaJ>()

        var total = 0

        for (alumno in alumnos) {

            if (soloConCita && alumno.tieneCita == null) {

                adaptador.actualizar(emptyList())

                txtAlumnosRecientes.setText(
                    R.string.buscar_alumno_filtro_no_disponible_j
                )

                return
            }

            if (soloConCita && alumno.tieneCita != true) {
                continue
            }

            total++

            val nombre = normalizar(alumno.nombre)
            val matricula = alumno.matricula

            if (busqueda.isEmpty() ||
                nombre.contains(busqueda) ||
                matricula.contains(busqueda)) {

                encontrados.add(alumno)
            }
        }

        adaptador.actualizar(encontrados)

        txtAlumnosRecientes.text = getString(
            R.string.buscar_alumno_contador_j,
            encontrados.size,
            total
        )
    }

    private fun normalizar(texto: String): String {

        val textoNormalizado = Normalizer.normalize(
            texto,
            Normalizer.Form.NFD
        )

        val textoSinAcentos = textoNormalizado.replace(
            Regex("\\p{M}+"),
            ""
        )

        return textoSinAcentos.lowercase(Locale.ROOT)
    }

    fun mostrarAlumnos(datos: List<dataAlumnoEnfermeriaJ>) {

        alumnos = datos

        filtrar(etBuscarAlumno.text.toString())
    }

    fun mostrarCargando(cargando: Boolean) {

        this.cargando = cargando

        etBuscarAlumno.isEnabled = !cargando
        spTipoBusqueda.isEnabled = !cargando

        if (cargando) {

            txtAlumnosRecientes.setText(
                R.string.buscar_alumno_consultando_j
            )

        } else {

            filtrar(etBuscarAlumno.text.toString())
        }
    }

    fun seleccionarAlumno(alumno: dataAlumnoEnfermeriaJ) {

        if (!cargando) {

            presentador.consultarCitas(token, alumno)
        }
    }

    fun mostrarOpcionesAtencion(
        alumno: dataAlumnoEnfermeriaJ,
        citas: List<dataCitaEnfermeriaJ>
    ) {

        if (isFinishing || isDestroyed) {
            return
        }

        val opciones = ArrayList<String>()

        for (cita in citas) {

            val informacion = cita.fecha + " · " +
                    cita.hora + " · " +
                    cita.motivo

            opciones.add(informacion)
        }

        opciones.add("Registrar signos sin cita")

        AlertDialog.Builder(this)
            .setTitle(alumno.nombre)
            .setItems(
                opciones.toTypedArray(),
                object : DialogInterface.OnClickListener {

                    override fun onClick(
                        dialog: DialogInterface?,
                        opcion: Int
                    ) {

                        val intent = Intent(
                            this@BuscarAlumnoActivityJ,
                            TomaSignosActivityJ::class.java
                        )

                        intent.putExtra("token", token)
                        intent.putExtra("correo", correo)
                        intent.putExtra("matricula", alumno.matricula)
                        intent.putExtra("nombre", alumno.nombre)
                        intent.putExtra("academico", alumno.informacionAcademica())

                        if (opcion < citas.size) {

                            intent.putExtra("id_cita", citas[opcion].idCita)
                        }

                        startActivity(intent)
                    }
                }
            )
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configurarNavegacion() {

        val menu = findViewById<BottomNavigationView>(
            R.id.bnvNavegacion
        )

        menu.menu.findItem(R.id.nav_campanias)?.isVisible = false

        menu.selectedItemId = R.id.nav_alumnos

        menu.setOnItemSelectedListener(
            object : NavigationBarView.OnItemSelectedListener {

                override fun onNavigationItemSelected(item: MenuItem): Boolean {

                    if (item.itemId == R.id.nav_inicio) {
                        finish()
                        return false
                    }

                    if (item.itemId == R.id.nav_alumnos) {
                        return true
                    }

                    return false
                }
            }
        )
    }

    fun mostrarError(mensaje: String) {

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {

        if (::presentador.isInitialized) {
            presentador.cancelar()
        }

        super.onDestroy()
    }
}