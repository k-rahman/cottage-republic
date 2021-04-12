package fi.oamk.cottagerepublic.ui.search

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository

class SearchViewModel(fragment: Fragment) : ViewModel() {
    private val dataSource = CottageRepository.getInstance(Firebase.database.getReference("cottages"))
    private var destination = SearchFragmentArgs.fromBundle(fragment.requireArguments()).destination

    private var _cottagesList = dataSource.getAllCottages()
    val cottagesList: LiveData<MutableList<Cottage>>
        get() = _cottagesList

    val searchQuery = MutableLiveData<String>()

    private val _navigateToCottageDetail = MutableLiveData<Cottage>()
    val navigateToCottageDetail: LiveData<Cottage>
        get() = _navigateToCottageDetail


    private val _isSearchBarFocused = MutableLiveData<Boolean>()
    val isSearchBarFocused: LiveData<Boolean>
        get() = _isSearchBarFocused

    fun onSearchItemClicked(cottage: Cottage) {
        _navigateToCottageDetail.value = cottage
    }

    fun onCottageDetailNavigated() {
        _navigateToCottageDetail.value = null
        searchQuery.value = ""
    }

    fun showKeyboard() {
        _isSearchBarFocused.value = true
        searchQuery.value = ""
    }

    fun closeKeyboard() {
        _isSearchBarFocused.value = false
    }

    fun checkForPassedArgs(): Boolean {
        if (destination != null) {
            searchQuery.value = "${destination!!.location["city"]}, ${destination!!.location["country"]}"
            destination = null
            return true
        }

        return false
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.removeListener()
    }
}
