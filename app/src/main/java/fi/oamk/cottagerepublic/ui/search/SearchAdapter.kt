/*
To display your data in a RecyclerView, you need the following parts:
 - RecyclerView: To create an instance of RecyclerView, define a <RecyclerView> element in the layout file.
 - LayoutManager: A RecyclerView uses a LayoutManager to organize the layout of the items in the RecyclerView,
    such as laying them out in a grid or in a linear list.
 - Layout for each item: Create a layout for one item of data in an XML layout file.
 - Adapter: Create an adapter that prepares the data and how it will be displayed in a ViewHolder. Associate the adapter
    with the RecyclerView.
 */

package fi.oamk.cottagerepublic.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.ListItemSearchBinding
import fi.oamk.cottagerepublic.util.CottageDiffCallBack

@Suppress("UNCHECKED_CAST")
class SearchAdapter(private val clickListener: SearchListListener) :
    ListAdapter<Cottage, SearchAdapter.ViewHolder>(CottageDiffCallBack()), Filterable {

    // reference to the current full list
    var fullList = listOf<Cottage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Cottage, clickListener: SearchListListener) {
            binding.cottageSearch = item
            binding.searchListListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSearchBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val query = charSequence.toString().toLowerCase().trim()
                val filteredList = mutableListOf<Cottage>()
                val results = FilterResults()

                // if query string is empty return the whole list
                if (charSequence.isEmpty()) {
                    results.count = fullList.size
                    results.values = fullList
                    return results
                }

                for (cottage in fullList) {
                    if (cottage.location["city"].isNullOrEmpty() || cottage.location["country"].isNullOrEmpty())
                        continue

                    val location =
                        "${cottage.location["city"]!!.toLowerCase()}, ${cottage.location["country"]!!.toLowerCase()}"

                    if (cottage.location["city"]!!.toLowerCase().startsWith(query) ||
                        cottage.location["country"]!!.toLowerCase().startsWith(query) ||
                        location.startsWith(query)
                    )
                        filteredList.add(cottage)
                }

                results.count = filteredList.size
                results.values = filteredList
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                if (filterResults.count  == 0)
                    submitList(mutableListOf())
                else
                    submitList(filterResults.values as MutableList<Cottage>)
            }
        }
    }
}

class SearchListListener(val clickListener: (cottage: Cottage) -> Unit) {
    fun onClick(cottage: Cottage) = clickListener(cottage)
}
