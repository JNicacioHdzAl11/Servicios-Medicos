package com.example.servicios_medicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.dataCampaniaJN
import com.google.android.material.button.MaterialButton

class campaniaAdapter(
    private var listaCampanias: List<dataCampaniaJN> = emptyList(),
    private val listener: OnCampaniaClickListener
) : RecyclerView.Adapter<campaniaAdapter.CampaniaViewHolder>() {

    interface OnCampaniaClickListener {
        fun onVerInformacionClick(campania: dataCampaniaJN)
    }

    fun actualizarLista(nuevaLista: List<dataCampaniaJN>) {
        this.listaCampanias = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampaniaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campania, parent, false)
        return CampaniaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampaniaViewHolder, position: Int) {
        holder.bind(listaCampanias[position])
    }

    override fun getItemCount(): Int = listaCampanias.size

    inner class CampaniaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgCampania: ImageView = itemView.findViewById(R.id.imgCampania)
        private val txtNombreCampania: TextView = itemView.findViewById(R.id.txtNombreCampania)
        private val txtFechaCampania: TextView = itemView.findViewById(R.id.txtFechaCampania)
        private val txtDetalleCampania: TextView = itemView.findViewById(R.id.txtDetalleCampania)
        private val btnVerInformacion: MaterialButton = itemView.findViewById(R.id.btnVerInformacion)

        fun bind(campania: dataCampaniaJN) {
            txtNombreCampania.text = campania.titulo

            val fechaCorta = formatearFechaCorta(campania.fechaInicio, campania.fechaFin)
            txtFechaCampania.text = "$fechaCorta · ${campania.horario}"
            txtDetalleCampania.text = "Actividad preventiva · ${campania.lugar}"

            imgCampania.setImageResource(R.drawable.ic_campania)

            btnVerInformacion.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.onVerInformacionClick(campania)
                }
            })
        }

        private fun formatearFechaCorta(inicio: String, fin: String): String {
            return try {
                val meses = mapOf(
                    "01" to "ene", "02" to "feb", "03" to "mar", "04" to "abr",
                    "05" to "may", "06" to "jun", "07" to "jul", "08" to "ago",
                    "09" to "sep", "10" to "oct", "11" to "nov", "12" to "dic"
                )
                val pIni = inicio.split("-")
                val pFin = fin.split("-")
                val diaIni = pIni[2].toInt()
                val diaFin = pFin[2].toInt()
                val mes = meses[pFin[1]] ?: pFin[1]
                if (diaIni == diaFin) "$diaIni $mes" else "$diaIni–$diaFin $mes"
            } catch (e: Exception) {
                inicio
            }
        }
    }
}