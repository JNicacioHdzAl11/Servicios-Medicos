package com.example.servicios_medicos.vista

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.dataCatalogosC
import com.example.servicios_medicos.modelo.dataOpcionCatalogoC
import com.example.servicios_medicos.presentador.clsCrearCuentaPresentadorC
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class CrearCuentaActivityC : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etMatricula: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText
    private lateinit var etConfirmarContrasena: TextInputEditText

    private lateinit var spnCarrera: Spinner
    private lateinit var spnGrupo: Spinner
    private lateinit var spnCuatrimestre: Spinner

    private lateinit var btnCrearCuenta: MaterialButton
    private lateinit var presentador: clsCrearCuentaPresentadorC

    private var catalogosCargados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta_c)

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

        etNombre = findViewById(R.id.etNombre)
        etMatricula = findViewById(R.id.etMatricula)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)

        spnCarrera = findViewById(R.id.spnCarrera)
        spnGrupo = findViewById(R.id.spnGrupo)
        spnCuatrimestre = findViewById(R.id.spnCuatrimestre)

        btnCrearCuenta = findViewById(R.id.btnCrearCuenta)

        presentador = clsCrearCuentaPresentadorC(this)

        configurarListeners()

        presentador.cargarCatalogos()
    }

    private fun configurarListeners() {

        btnCrearCuenta.setOnClickListener(
            object : View.OnClickListener {

                override fun onClick(v: View?) {

                    val nombre = etNombre.text.toString()
                    val matricula = etMatricula.text.toString()
                    val correo = etCorreo.text.toString()
                    val contrasena = etContrasena.text.toString()
                    val confirmar = etConfirmarContrasena.text.toString()

                    val carrera = spnCarrera.selectedItem as? dataOpcionCatalogoC
                    val grupo = spnGrupo.selectedItem as? dataOpcionCatalogoC
                    val cuatrimestre = spnCuatrimestre.selectedItem as? dataOpcionCatalogoC

                    if (carrera == null || grupo == null || cuatrimestre == null) {

                        mostrarError("Selecciona los campos académicos")
                        return
                    }

                    presentador.crearCuenta(
                        nombre,
                        matricula,
                        correo,
                        contrasena,
                        confirmar,
                        carrera.id,
                        grupo.id,
                        cuatrimestre.id
                    )
                }
            }
        )
    }

    private fun llenarSpinner(
        spinner: Spinner,
        datos: List<dataOpcionCatalogoC>,
        mensaje: String
    ) {

        val opciones = ArrayList<dataOpcionCatalogoC>()

        opciones.add(dataOpcionCatalogoC(0, mensaje))
        opciones.addAll(datos)

        val adaptador = ArrayAdapter(
            this,
            R.layout.item_spinner_c,
            opciones
        )

        adaptador.setDropDownViewResource(
            R.layout.item_spinner_c
        )

        spinner.adapter = adaptador
    }

    fun mostrarCatalogos(datos: dataCatalogosC) {

        llenarSpinner(
            spnCarrera,
            datos.carreras,
            "Selecciona tu carrera"
        )

        llenarSpinner(
            spnGrupo,
            datos.grupos,
            "Selecciona tu grupo"
        )

        llenarSpinner(
            spnCuatrimestre,
            datos.cuatrimestres,
            "Selecciona tu cuatrimestre"
        )

        catalogosCargados = datos.carreras.isNotEmpty() &&
                datos.grupos.isNotEmpty() &&
                datos.cuatrimestres.isNotEmpty()

        if (!catalogosCargados) {
            mostrarError("Faltan opciones académicas en el servidor")
        }
    }

    fun mostrarCargandoCatalogos(cargando: Boolean) {

        spnCarrera.isEnabled = !cargando
        spnGrupo.isEnabled = !cargando
        spnCuatrimestre.isEnabled = !cargando

        btnCrearCuenta.isEnabled = !cargando && catalogosCargados

        if (cargando) {

            btnCrearCuenta.text = "Cargando opciones..."

        } else {

            btnCrearCuenta.text = "Crear cuenta"
        }
    }

    fun mostrarCargando(cargando: Boolean) {

        btnCrearCuenta.isEnabled = !cargando && catalogosCargados

        spnCarrera.isEnabled = !cargando
        spnGrupo.isEnabled = !cargando
        spnCuatrimestre.isEnabled = !cargando

        if (cargando) {

            btnCrearCuenta.text = "Creando cuenta..."

        } else {

            btnCrearCuenta.text = "Crear cuenta"
        }
    }

    fun mostrarCuentaCreada() {

        Toast.makeText(
            this,
            "Cuenta creada correctamente",
            Toast.LENGTH_LONG
        ).show()

        finish()
    }

    fun mostrarError(mensaje: String) {

        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }
}