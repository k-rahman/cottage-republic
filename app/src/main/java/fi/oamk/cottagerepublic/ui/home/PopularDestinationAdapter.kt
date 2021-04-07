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
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.ListItemPopularDestinationBinding

// Data
data class Destination(
    val image: Int = R.drawable.ic_launcher_background,
    val destinationName: String = "testLabel"
)

class PopularDestinationAdapter(val clickListener: DestinationListener) : RecyclerView.Adapter<PopularDestinationAdapter.ViewHolder>() {

    val data = listOf<Destination>(Destination(), Destination(), Destination())

    // used by RecyleView to get the number of items it will render
    override fun getItemCount(): Int = data.size

    // used by RecycleView to get the ViewHolder (Wrapper around list item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // used by RecycleView to get access each list item and bind it with its data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, clickListener)
    }

    // wrapper around the list item (the card view in this case)
    // ViewHolder is an private inner class of PopularDestinationAdapter class
    class ViewHolder private constructor(val binding: ListItemPopularDestinationBinding) : RecyclerView.ViewHolder(binding.root) {

        // binding data in ViewHolder is a better practice
        fun bind(item: Destination, clickListener: DestinationListener) {
//            binding.cardImage.setImageResource(item.image)
//            binding.cardLabel.text = item.destinationName
            binding.destination = item
            binding.destinationListener = clickListener
            binding.executePendingBindings()
        }

        // companion object is the same as static keyword in Java
        companion object {
            // The from() function needs to be in a companion object,
            // so it can be called on the ViewHolder class, not called on a ViewHolder instance.
            fun from(parent: ViewGroup): ViewHolder {
                // Inflation should happen in the ViewHolder
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPopularDestinationBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

// handles click on recycleView item
class DestinationListener(val clickListener: (destinationName: String) -> Unit) {
    fun onClick(destination: Destination) = clickListener(destination.destinationName)
}