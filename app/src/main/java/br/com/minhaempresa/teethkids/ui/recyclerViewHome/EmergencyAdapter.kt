package br.com.minhaempresa.teethkids.ui.recyclerViewHome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemEmergenciaBinding

class EmergencyAdapter(private val emergencies: List<Emergency>, private val listener: RecyclerViewEvent)
    : RecyclerView.Adapter<EmergencyViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemEmergenciaBinding.inflate(from, parent, false)
        return EmergencyViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = emergencies.size

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        holder.bindEmergency(emergencies[position])
    }

    interface RecyclerViewEvent{
        fun onItemClick(position: Int)
    }
}