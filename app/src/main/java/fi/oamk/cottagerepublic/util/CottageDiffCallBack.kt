package fi.oamk.cottagerepublic.util

import androidx.recyclerview.widget.DiffUtil
import fi.oamk.cottagerepublic.data.Cottage

class CottageDiffCallBack : DiffUtil.ItemCallback<Cottage>() {
    override fun areItemsTheSame(oldItem: Cottage, newItem: Cottage): Boolean {
        return oldItem.cottageId == newItem.cottageId
    }

    override fun areContentsTheSame(oldItem: Cottage, newItem: Cottage): Boolean {
        return oldItem == newItem
    }
}
