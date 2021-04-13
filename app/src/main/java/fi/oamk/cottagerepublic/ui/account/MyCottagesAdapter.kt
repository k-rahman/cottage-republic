/*
To display your data in a RecyclerView, you need the following parts:
 - RecyclerView: To create an instance of RecyclerView, define a <RecyclerView> element in the layout file.
 - LayoutManager: A RecyclerView uses a LayoutManager to organize the layout of the items in the RecyclerView,
    such as laying them out in a grid or in a linear list.
 - Layout for each item: Create a layout for one item of data in an XML layout file.
 - Adapter: Create an adapter that prepares the data and how it will be displayed in a ViewHolder. Associate the adapter
    with the RecyclerView.
 */

package fi.oamk.cottagerepublic.ui.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.ListItemMyCottagesBinding


// Data
data class NewCottage(
//    val image: Int = R.drawable.ic_launcher_background,
    val cottageName: String = "testLabel"
)
class MyCottagesAdapter(val clickListener: MyCottageListener): RecyclerView.Adapter<MyCottagesAdapter.ViewHolder>() {

    val data = listOf<NewCottage>(NewCottage(), NewCottage(), NewCottage())

    // used by RecycleView to get the number of items it will render
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
    // ViewHolder is a private inner class of MyCottagesAdapter class
    class ViewHolder private constructor(val binding: ListItemMyCottagesBinding) : RecyclerView.ViewHolder(binding.root) {

        // binding data in ViewHolder is a better practice
        fun bind(item: NewCottage, clickListener: MyCottageListener) {
            binding.newCottage = item
            binding.myCottageListener = clickListener
            binding.executePendingBindings()
        }

        // companion object is the same as static keyword in Java
        companion object {
            // The from() function needs to be in a companion object,
            // so it can be called on the ViewHolder class, not called on a ViewHolder instance.
            fun from(parent: ViewGroup): ViewHolder {
                // Inflation should happen in the ViewHolder
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemMyCottagesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

// handles click on recycleView item
class MyCottageListener(val clickListener: (cottageName: String) -> Unit) {
    fun onClick(newCottage: NewCottage) = clickListener(newCottage.cottageName)
}