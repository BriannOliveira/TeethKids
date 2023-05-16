package br.com.minhaempresa.teethkids.ui.recyclerViewHome

import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemEmergenciaBinding

class EmergencyViewHolder(
    private val itemEmergenciaBinding: ItemEmergenciaBinding,
    private val clickListener: EmergencyClickListener
): RecyclerView.ViewHolder(itemEmergenciaBinding.root) {

    fun bindEmergency(emergency: Emergency){
        itemEmergenciaBinding.itemEmergenciaTitulo.text = emergency.nameUser
        itemEmergenciaBinding.itemEmergenciaDescricao.text = emergency.phone

        itemEmergenciaBinding.cvEmergency.setOnClickListener{
            clickListener.onCLick(emergency)
        }
    }
}