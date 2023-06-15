package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemAvaliationBinding
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
            itemAvaliationBinding.itemReputationName.text = avaliation.name
            itemAvaliationBinding.itemReputationRatingbar.rating = avaliation.rate.toFloat()
            itemAvaliationBinding.itemReputationValueRating.text = "${avaliation.rate}"
            if(avaliation.time.toMinutesFromNow() < 1){
                itemAvaliationBinding.itemReputationTime.setText("Agora")
            } //minutos
            else if (avaliation.time.toMinutesFromNow() >= 1 && avaliation.time.toMinutesFromNow() < 60){
                itemAvaliationBinding.itemReputationTime.setText("${avaliation.time.toMinutesFromNow().toInt()} min atrás")
            } //horas
            else if(avaliation.time.toMinutesFromNow() >= 60 && avaliation.time.toMinutesFromNow() < 1440){
                itemAvaliationBinding.itemReputationTime.setText("${avaliation.time.toMinutesFromNow().toInt()/60}h atrás")
            } //dias
            else if(avaliation.time.toMinutesFromNow() >= 1440 && avaliation.time.toMinutesFromNow() < 43200){
                itemAvaliationBinding.itemReputationTime.setText("${avaliation.time.toMinutesFromNow().toInt()/1440}d atrás")
            } else {
                itemAvaliationBinding.itemReputationTime.setText("${avaliation.time.minutesToDate(avaliation.time.toMinutesFromNow().toInt())}")
            }
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