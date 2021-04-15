package fi.oamk.cottagerepublic.util

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object NumberOfGuests {

    // sets the list
    @BindingAdapter("entries")
    @JvmStatic
    fun Spinner.setEntries(items: List<String>) {
        if (items != null) {
            val arrayAdapter = ArrayAdapter(context, R.layout.simple_spinner_item, items)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = arrayAdapter
        }
    }

    // required but not used. If you were to update viewModel.numberOfGuests dynamically, you would use it
    @BindingAdapter("numberOfGuests")
    @JvmStatic
    fun Spinner.setNumberOfGuests(item: String) {
    }

    // the returned value is stored in viewModel.numberOfGuests
    @InverseBindingAdapter(attribute = "numberOfGuests")
    @JvmStatic
    fun Spinner.getNumberOfGuests(): String {
        return selectedItem.toString()
            .removeSuffix("Guest")
            .removeSuffix("Guests")
            .trim()
    }

    // called when user select an item
    @BindingAdapter("numberOfGuestsAttrChanged")
    @JvmStatic
    fun Spinner.setListener(listener: InverseBindingListener?) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listener?.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}