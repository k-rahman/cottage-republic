package fi.oamk.cottagerepublic.ui.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.databinding.ListItemCottageImageFullScreenBinding
import fi.oamk.cottagerepublic.util.StringDiffCallBack

class ImageAdapter() :
    ListAdapter<String, ImageAdapter.ViewHolder>(StringDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListItemCottageImageFullScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.imageUrl = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCottageImageFullScreenBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}