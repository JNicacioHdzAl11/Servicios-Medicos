package com.example.servicios_medicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.Data.dataMedicamentoJN

class clsMedicamentoRecetaAdaptador(
    private var listaMedicamentos: List<dataMedicamentoJN> = emptyList()
) : RecyclerView.Adapter<clsMedicamentoRecetaAdaptador.MedicamentoViewHolder>() {

    class MedicamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombreMedicamento: TextView = itemView.findViewById(R.id.txtNombreMedicamento)
        val txtConcentracion: TextView = itemView.findViewById(R.id.txtConcentracion)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidad)
        val txtViaAdministracion: TextView = itemView.findViewById(R.id.txtViaAdministracion)
        val txtFrecuencia: TextView = itemView.findViewById(R.id.txtFrecuencia)
        val txtHorario: TextView = itemView.findViewById(R.id.txtHorario)
        val txtDuracion: TextView = itemView.findViewById(R.id.txtDuracion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentoViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicamento_receta, parent, false)
        return MedicamentoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: MedicamentoViewHolder, position: Int) {
        val med = listaMedicamentos[position]
        holder.txtNombreMedicamento.text = med.nombre
        holder.txtConcentracion.text = med.concentracion
        holder.txtCantidad.text = med.dosis
        holder.txtViaAdministracion.text = med.via
        holder.txtFrecuencia.text = med.frecuencia
        holder.txtHorario.text = med.horarios
        holder.txtDuracion.text = med.duracion
    }

    override fun getItemCount(): Int = listaMedicamentos.size

    fun actualizarLista(nuevaLista: List<dataMedicamentoJN>) {
        listaMedicamentos = nuevaLista
        notifyDataSetChanged()
    }
}