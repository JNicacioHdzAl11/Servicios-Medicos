package com.example.servicios_medicos.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.Data.dataAlumnoEnfermeriaJ
import com.example.servicios_medicos.vista.BuscarAlumnoActivityJ

class clsAlumnosEnfermeriaAdaptadorJ(private val vista: BuscarAlumnoActivityJ) : RecyclerView.Adapter<clsAlumnosEnfermeriaAdaptadorJ.Fila>() {
    private val alumnos = ArrayList<dataAlumnoEnfermeriaJ>()
    class Fila(view: View) : RecyclerView.ViewHolder(view) {
        val iniciales: TextView = view.findViewById(R.id.txtIniciales)
        val nombre: TextView = view.findViewById(R.id.txtNombreAlumno)
        val matricula: TextView = view.findViewById(R.id.txtMatricula)
        val carrera: TextView = view.findViewById(R.id.txtCarreraGrupo)
        val boton: View = view.findViewById(R.id.btnIniciarAtencion)
    }
    fun actualizar(datos: List<dataAlumnoEnfermeriaJ>) { alumnos.clear(); alumnos.addAll(datos); notifyDataSetChanged() }
    override fun getItemCount(): Int = alumnos.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Fila = Fila(LayoutInflater.from(parent.context).inflate(R.layout.item_alumno, parent, false))
    override fun onBindViewHolder(holder: Fila, position: Int) {
        val alumno = alumnos[position]
        var iniciales = ""
        for (parte in alumno.nombre.split(" ")) {
            if (parte.isNotBlank() && iniciales.length < 2) iniciales += parte.take(1).uppercase()
        }
        holder.iniciales.text = iniciales
        holder.nombre.text = alumno.nombre
        holder.matricula.text = "Matrícula ${alumno.matricula}"
        holder.carrera.text = alumno.informacionAcademica()
        holder.boton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) { vista.seleccionarAlumno(alumno) }
        })
    }
}
