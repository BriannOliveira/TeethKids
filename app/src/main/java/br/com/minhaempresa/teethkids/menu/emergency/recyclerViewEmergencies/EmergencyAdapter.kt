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
            itemEmergenciaBinding.itemEmergenciaTitulo.text = emergency.name
            itemEmergenciaBinding.itemEmergenciaDescricao.text = emergency.phoneNumber
            //colocar foto
            if(emergency.time.toMinutesFromNow() < 1){
                itemEmergenciaBinding.itemEmergenciaTempo.setText("Agora")
            } else if (emergency.time.toMinutesFromNow() >= 1 && emergency.time.toMinutesFromNow() < 60){
                itemEmergenciaBinding.itemEmergenciaTempo.setText("${emergency.time.toMinutesFromNow().toInt()} min atrás")
            } else{
                itemEmergenciaBinding.itemEmergenciaTempo.setText("${emergency.time.toMinutesFromNow().toInt()/60}h atrás")
            }

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

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
        return (emergencies[position].time._seconds * 1000).toLong() + (emergencies[position].time._nanoseconds * 1000000).toLong()
    }

    interface RecyclerViewEvent{
        fun onItemClick(position: Int)
    }
}