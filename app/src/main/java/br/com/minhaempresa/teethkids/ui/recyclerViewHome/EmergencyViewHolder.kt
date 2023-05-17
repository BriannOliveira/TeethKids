package br.com.minhaempresa.teethkids.ui.recyclerViewHome

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewbinding.ViewBindings
import br.com.minhaempresa.teethkids.databinding.ItemEmergenciaBinding

class EmergencyViewHolder(
    private val itemEmergenciaBinding: ItemEmergenciaBinding,
    private val listener: EmergencyAdapter.RecyclerViewEvent
) : RecyclerView.ViewHolder(itemEmergenciaBinding.root), View.OnClickListener {

    fun bindEmergency(emergency: Emergency){
        itemEmergenciaBinding.itemEmergenciaTitulo.text = emergency.nameUser
        itemEmergenciaBinding.itemEmergenciaDescricao.text = emergency.phone
        itemEmergenciaBinding.itemEmergenciaFoto.setImageResource(emergency.photo)
    }

    init {
        itemEmergenciaBinding.cvEmergency.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION){
            listener.onItemClick(position)
        }
    }
}