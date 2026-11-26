package com.example.servicios_medicos.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.clsResumenEnfermeriaModeloJ
import com.example.servicios_medicos.presentador.clsInicioEnfermeraPresentadorJ
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InicioEnfermera : Fragment() {
    private lateinit var presentador: clsInicioEnfermeraPresentadorJ
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_inicio_enfermera, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actividad = requireActivity() as EnfermeraMainActivityJ
        view.findViewById<TextView>(R.id.txtSaludoEnfermera).text = "Bienvenida"
        view.findViewById<TextView>(R.id.txtCedulaEnfermera).text = "Personal de enfermería"
        view.findViewById<TextView>(R.id.txtFechaActual).text = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "MX")).format(Date())
        view.findViewById<View>(R.id.btnNuevaAtencionJ).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) { actividad.abrirAlumnos(false) }
        })
        view.findViewById<View>(R.id.cardCitasHoy).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) { actividad.abrirAlumnos(true) }
        })
        view.findViewById<View>(R.id.btnConfiguracion).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) { actividad.abrirConfiguracion() }
        })
        presentador = clsInicioEnfermeraPresentadorJ(this)
    }
    override fun onResume() {
        super.onResume()
        presentador.cargar(requireActivity().intent.getStringExtra("token").orEmpty())
    }
    fun mostrarCitas(total: String) { view?.findViewById<TextView>(R.id.txtTotalCitasHoy)?.text = total }
    fun mostrarAtenciones(datos: List<clsResumenEnfermeriaModeloJ.Atencion>) {
        val raiz = view ?: return
        raiz.findViewById<TextView>(R.id.txtTotalAtenciones).text = datos.size.toString()
        val lista = raiz.findViewById<LinearLayout>(R.id.listaAtencionesJ)
        lista.removeAllViews()
        if (datos.isEmpty()) {
            val texto = TextView(requireContext())
            texto.text = "Sin valoraciones registradas en esta sesión."
            val espacio = (16 * resources.displayMetrics.density).toInt()
            texto.setPadding(espacio, espacio, espacio, espacio)
            texto.setTextColor(androidx.core.content.ContextCompat.getColor(requireContext(), R.color.color_texto_secundario))
            texto.textSize = 14f
            lista.addView(texto)
        }
        for (dato in datos.take(5)) {
            val fila = layoutInflater.inflate(R.layout.item_atencion_reciente, lista, false)
            fila.findViewById<TextView>(R.id.txtInicialesAtencion).text = dato.nombre.take(2).uppercase()
            fila.findViewById<TextView>(R.id.txtNombreAtencion).text = dato.nombre
            fila.findViewById<TextView>(R.id.txtDetalleAtencion).text = "Signos guardados · " + dato.hora
            lista.addView(fila)
        }
    }
    override fun onDestroyView() { presentador.cancelar(); super.onDestroyView() }
}
