package fi.oamk.cottagerepublic.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination

class SearchViewModelFactory(private val cottage: Cottage?, private val destination: Destination?) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(cottage, destination) as T
        }
        throw IllegalArgumentException("Unknown Class")
    }

}