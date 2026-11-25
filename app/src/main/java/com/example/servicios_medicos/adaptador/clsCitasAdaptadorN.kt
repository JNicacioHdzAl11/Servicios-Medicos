package com.example.servicios_medicos.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.servicios_medicos.R
import com.example.servicios_medicos.modelo.TarjetaCitaN
import com.example.servicios_medicos.vista.CitasFragmentN

class clsCitasAdaptadorN(private val tarjetas: List<TarjetaCitaN>, private val vista: CitasFragmentN) :
    RecyclerView.Adapter<clsCitasAdaptadorN.TarjetaVista>() {

    class TarjetaVista(item: View) : RecyclerView.ViewHolder(item) {
        val txtTitulo: TextView = item.findViewById(R.id.txtCitaTitulo)
        val txtEstado: TextView = item.findViewById(R.id.txtCitaEstado)
        val txtFecha: TextView = item.findViewById(R.id.txtCitaFecha)
        val txtDoctora: TextView = item.findViewById(R.id.txtCitaDoctora)
        val txtVerDetalle: TextView = item.findViewById(R.id.txtCitaVerDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaVista { // crea una tarjeta con el diseño item_cita_n
        val diseno = LayoutInflater.from(parent.context).inflate(R.layout.item_cita_n, parent, false)
        return TarjetaVista(diseno)
    }

    override fun getItemCount(): Int { // dice cuantas tarjetas hay
        return tarjetas.size
    }

    override fun onBindViewHolder(holder: TarjetaVista, position: Int) { // llena la tarjeta con los datos de una cita
        val tarjeta = tarjetas[position]
        holder.txtTitulo.text = tarjeta.motivo
        holder.txtEstado.text = tarjeta.estado
        holder.txtFecha.text = tarjeta.fecha
        holder.txtDoctora.text = tarjeta.doctora

        var color = R.color.color_texto_secundario
        if (tarjeta.estado == "Pendiente") {
            color = R.color.color_exito
        }
        if (tarjeta.estado == "Cancelada") {
            color = R.color.color_peligro
        }
        holder.txtEstado.setTextColor(ContextCompat.getColor(holder.itemView.context, color))

        holder.itemView.setOnClickListener {
            vista.tocarCita(position)
        }
        holder.txtVerDetalle.setOnClickListener {
            vista.tocarCita(position)
        }
    }
}
