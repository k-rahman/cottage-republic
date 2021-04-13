package fi.oamk.cottagerepublic.ui.search

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(private val fragment: Fragment) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(fragment) as T
        }
        throw IllegalArgumentException("Unknown Class")
    }

}