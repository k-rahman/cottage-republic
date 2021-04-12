package fi.oamk.cottagerepublic.ui.cottageDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.databinding.ListItemCottageAmenityBinding
import fi.oamk.cottagerepublic.util.StringDiffCallBack

class AmenityAdapter : ListAdapter<String, AmenityAdapter.AmenitiesViewHolder>(StringDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesViewHolder {
        return AmenitiesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AmenitiesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AmenitiesViewHolder(var binding: ListItemCottageAmenityBinding) : RecyclerView.ViewHolder(binding.root) {
       fun bind(item: String){
           binding.amenity = item
           binding.executePendingBindings()
       }

        companion object {
            fun from(parent: ViewGroup) : AmenitiesViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCottageAmenityBinding.inflate(layoutInflater, parent, false )
                return AmenitiesViewHolder(binding)
            }
        }
    }
}