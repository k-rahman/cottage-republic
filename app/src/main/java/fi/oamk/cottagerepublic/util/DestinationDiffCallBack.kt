package fi.oamk.cottagerepublic.util

import androidx.recyclerview.widget.DiffUtil
import fi.oamk.cottagerepublic.data.Destination

class DestinationDiffCallBack : DiffUtil.ItemCallback<Destination>() {
    override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean {
        return oldItem == newItem
    }
}
