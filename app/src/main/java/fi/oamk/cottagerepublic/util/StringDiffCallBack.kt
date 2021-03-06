package fi.oamk.cottagerepublic.util

import androidx.recyclerview.widget.DiffUtil

class StringDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
       return oldItem== newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}