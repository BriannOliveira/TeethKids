package br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.minhaempresa.teethkids.databinding.ItemViewPagerEmergencyBinding
import com.bumptech.glide.Glide

class ImageAdapter(
    private val context: Context,
    private val imageUrls: List<String>
    ) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(
        itemViewPagerBinding: ItemViewPagerEmergencyBinding
    ) : RecyclerView.ViewHolder(itemViewPagerBinding.root) {
        val imageView: ImageView = itemViewPagerBinding.ivPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewPagerBinding= ItemViewPagerEmergencyBinding.inflate(inflater, parent, false)
        return ImageViewHolder(viewPagerBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(imageUrls[position])
            .into(holder.imageView)
    }

    override fun getItemCount() = imageUrls.size
}