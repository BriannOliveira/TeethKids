package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemAvaliationBinding
import br.com.minhaempresa.teethkids.databinding.ItemEmergenciaBinding
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter

class AvaliationAdapter(
    private val avaliations: List<Avaliation>,
    private val listener: EmergencyAdapter.RecyclerViewEvent
) : RecyclerView.Adapter<AvaliationAdapter.AvaliationViewHolder>()
{


    //classe do ViewHolder
    inner class AvaliationViewHolder(
        private val itemAvaliationBinding: ItemAvaliationBinding
    ) : RecyclerView.ViewHolder(itemAvaliationBinding.root), View.OnClickListener {

        //função de vincular dados ao ViewHolder
        fun bindAvaliatons(avaliation: Avaliation){
            itemAvaliationBinding.itemReputationName.text = avaliation.nameUser
            itemAvaliationBinding.itemReputationTime.text = avaliation.time
            itemAvaliationBinding.itemReputationRatingbar.rating = avaliation.rating.toFloat()
            itemAvaliationBinding.itemReputationValueRating.text = "${avaliation.rating}"

        }

        init {
            itemAvaliationBinding.itemReputationCvAvaliation.setOnClickListener(this)
        }


        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
            {
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliationViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ItemAvaliationBinding.inflate(from, parent, false)
        return AvaliationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return avaliations.size
    }

    override fun onBindViewHolder(holder: AvaliationViewHolder, position: Int) {
        holder.bindAvaliatons(avaliations[position])
    }
}