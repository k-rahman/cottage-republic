package fi.oamk.cottagerepublic.ui.cottageDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.databinding.ListItemCottageImageBinding
import fi.oamk.cottagerepublic.util.StringDiffCallBack

class CottageImageAdapter(private val clickListener: CottageImageListener) :
    ListAdapter<String, CottageImageAdapter.ViewHolder>(StringDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemCottageImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, clickListener: CottageImageListener) {
            binding.imageUrl = item
            binding.cottageImageListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCottageImageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class CottageImageListener(val clickListener: (imageUrl: String) -> Unit) {
    fun onClick(imageUrl: String) = clickListener(imageUrl)
}
