/*
To display your data in a RecyclerView, you need the following parts:
 - RecyclerView: To create an instance of RecyclerView, define a <RecyclerView> element in the layout file.
 - LayoutManager: A RecyclerView uses a LayoutManager to organize the layout of the items in the RecyclerView,
    such as laying them out in a grid or in a linear list.
 - Layout for each item: Create a layout for one item of data in an XML layout file.
 - Adapter: Create an adapter that prepares the data and how it will be displayed in a ViewHolder. Associate the adapter
    with the RecyclerView.
 */

package fi.oamk.cottagerepublic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.databinding.ListItemPopularCottagesBinding

// Data
data class Cottage(
    val image: Int = R.drawable.ic_launcher_background,
    val cottageLabel: String = "testLabel"
)

class PopularCottagesAdapter(val clickListener: CottageListener) : RecyclerView.Adapter<PopularCottagesAdapter.ViewHolder>() {

    val data = listOf(Cottage(), Cottage(), Cottage())

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
    class ViewHolder private constructor(val binding: ListItemPopularCottagesBinding) : RecyclerView.ViewHolder(binding.root) {

        // binding data in ViewHolder is a better practice
        fun bind(item: Cottage, clickListener: CottageListener) {
//            binding.cardImage.setImageResource(item.image)
//            binding.cardLabel.text = item.label
            binding.cottage = item
            binding.cottageListener = clickListener
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

class CottageListener(val clickListener: (cottageLabel: String) -> Unit) {
    fun onClick(cottage: Cottage) = clickListener(cottage.cottageLabel)
}
