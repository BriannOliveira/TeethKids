package br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemEmergenciaBinding

//classe do Adapter
class EmergencyAdapter(
    private val emergencies: List<Emergency>,
    private val listener: RecyclerViewEvent
    ) : RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder>()
{

    //classe do ViewHolder
    inner class EmergencyViewHolder(
        private val itemEmergenciaBinding: ItemEmergenciaBinding, //vinculação do layout do item ao ViewHolder
    ) : RecyclerView.ViewHolder(itemEmergenciaBinding.root), View.OnClickListener {

        //função de vincular dados ao ViewHolder
        fun bindEmergency(emergency: Emergency){
            itemEmergenciaBinding.itemEmergenciaTitulo.text = emergency.nameUser
            itemEmergenciaBinding.itemEmergenciaDescricao.text = emergency.phone
            itemEmergenciaBinding.itemEmergenciaFoto.setImageResource(emergency.photo)
            itemEmergenciaBinding.itemEmergenciaTempo.setText("${emergency.time.toString()} min atrás")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        //inflando o Layout de cada item
        val from = LayoutInflater.from(parent.context)
        val binding = ItemEmergenciaBinding.inflate(from, parent, false)
        return EmergencyViewHolder(binding)
    }

    override fun getItemCount(): Int = emergencies.size

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        holder.bindEmergency(emergencies[position])
    }

    interface RecyclerViewEvent{
        fun onItemClick(position: Int)
    }
}