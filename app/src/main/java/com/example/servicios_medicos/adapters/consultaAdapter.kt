package com.example.servicios_medicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.dataConsultaJ
import com.google.android.material.button.MaterialButton

class consultaAdapter(
    private var listaConsultas: List<dataConsultaJ> = emptyList(),
    private val listener: OnConsultaClickListener
) : RecyclerView.Adapter<consultaAdapter.ConsultaViewHolder>() {

    interface OnConsultaClickListener {
        fun onDescargarRecetaClick(consulta: dataConsultaJ)
        fun onConsultaItemClick(consulta: dataConsultaJ)
    }

    fun actualizarLista(nuevaLista: List<dataConsultaJ>) {
        this.listaConsultas = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consulta, parent, false)
        return ConsultaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsultaViewHolder, position: Int) {
        holder.bind(listaConsultas[position])
    }

    override fun getItemCount(): Int = listaConsultas.size

    inner class ConsultaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        private val txtServicio: TextView = itemView.findViewById(R.id.txtServicio)
        private val txtMedico: TextView = itemView.findViewById(R.id.txtMedico)
        private val btnDescargarReceta: MaterialButton = itemView.findViewById(R.id.btnDescargarReceta)

        fun bind(consulta: dataConsultaJ) {
            txtFecha.text = formatearFechaCorta(consulta.fechaConsulta)
            txtServicio.text = if (consulta.motivo.isNotBlank()) consulta.motivo else "Consulta médica"

            val doctoraTexto = if (!consulta.doctora.isNullOrBlank()) consulta.doctora.trim() else "Médico asignado"
            txtMedico.text = "$doctoraTexto · Consulta concluida"

            if (consulta.tieneReceta && consulta.idReceta != null) {
                btnDescargarReceta.visibility = View.VISIBLE
                btnDescargarReceta.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        listener.onDescargarRecetaClick(consulta)
                    }
                })
            } else {
                btnDescargarReceta.visibility = View.GONE
            }

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onConsultaItemClick(consulta)
                }
            })
        }

        private fun formatearFechaCorta(fechaRaw: String): String {
            return try {
                val meses = mapOf(
                    "01" to "ene", "02" to "feb", "03" to "mar", "04" to "abr",
                    "05" to "may", "06" to "jun", "07" to "jul", "08" to "ago",
                    "09" to "sep", "10" to "oct", "11" to "nov", "12" to "dic"
                )
                val soloFecha = fechaRaw.split(" ")[0]
                val partes = soloFecha.split("-")
                val dia = partes[2]
                val mes = meses[partes[1]] ?: partes[1]
                val anio = partes[0]
                "$dia $mes $anio"
            } catch (e: Exception) {
                fechaRaw
            }
        }
    }
}