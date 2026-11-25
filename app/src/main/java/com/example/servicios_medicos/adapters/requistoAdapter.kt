package com.example.servicios_medicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R

class requisitoAdapter(
    private var listaRequisitos: List<String> = emptyList()
) : RecyclerView.Adapter<requisitoAdapter.RequisitoViewHolder>() {

    fun actualizarLista(nuevaLista: List<String>) {
        this.listaRequisitos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequisitoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_requisito, parent, false)
        return RequisitoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequisitoViewHolder, position: Int) {
        holder.bind(listaRequisitos[position])
    }

    override fun getItemCount(): Int = listaRequisitos.size

    inner class RequisitoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtRequisito: TextView = itemView.findViewById(R.id.txtRequisito)

        fun bind(texto: String) {
            txtRequisito.text = texto
        }
    }
}