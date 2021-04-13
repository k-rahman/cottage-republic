/*
To display your data in a RecyclerView, you need the following parts:
 - RecyclerView: To create an instance of RecyclerView, define a <RecyclerView> element in the layout file.
 - LayoutManager: A RecyclerView uses a LayoutManager to organize the layout of the items in the RecyclerView,
    such as laying them out in a grid or in a linear list.
 - Layout for each item: Create a layout for one item of data in an XML layout file.
 - Adapter: Create an adapter that prepares the data and how it will be displayed in a ViewHolder. Associate the adapter
    with the RecyclerView.
 */

package fi.oamk.cottagerepublic.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.ListItemPopularCottagesBinding
import fi.oamk.cottagerepublic.util.CottageDiffCallBack

class PopularCottagesAdapter(private val clickListener: CottageListener) :
    ListAdapter<Cottage, PopularCottagesAdapter.ViewHolder>(CottageDiffCallBack()) {

    // used by RecycleView to get the ViewHolder (Wrapper around list item)
    // The parent parameter, which is the view group that holds the view holder, is always the RecyclerView
    // The viewType parameter is used when there are multiple views in the same RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // the onBindViewHolder()function is called by RecyclerView to display the data for one list item at the specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    // wrapper around the list item (the card view in this case)
    // ViewHolder is an private inner class of PopularDestinationAdapter class
    class ViewHolder private constructor(val binding: ListItemPopularCottagesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // binding data in ViewHolder is a better practice
        fun bind(item: Cottage, clickListener: CottageListener) {
            binding.cottage = item
            binding.cottageListener = clickListener

            // it's always a good idea to call executePendingBindings() when you use binding adapters in a RecyclerView,
            // because it can slightly speed up sizing the views.
            binding.executePendingBindings()
        }

        // companion object is the same as static keyword in Java
        companion object {
            // The from() function needs to be in a companion object,
            // so it can be called on the ViewHolder class, not called on a ViewHolder instance.
            fun from(parent: ViewGroup): ViewHolder {
                // Inflation should happen in the ViewHolder
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPopularCottagesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class CottageListener(val clickListener: (cottage: Cottage) -> Unit) {
    fun onClick(cottage: Cottage) = clickListener(cottage)
}
