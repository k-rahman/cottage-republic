package fi.oamk.cottagerepublic.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository

class SearchViewModel : ViewModel() {
    private val dataSource = CottageRepository.getInstance(Firebase.database.getReference("cottages"))

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

    fun searchByLocation(query: String?) {
        val filteredList = _cottagesList.value?.filter {
            it.location.equals(query.toString(), ignoreCase = true)
        }!!


        for (cottage in filteredList)
            _cottagesList.value?.add(cottage)
//        if (query.isNullOrEmpty())
//            _cottagesList = dataSource.getAllCottages()
//
//        else
//            _cottagesList = dataSource.searchByLocation(query)
    }

    fun onSearchItemClicked(cottage: Cottage) {
        _navigateToCottageDetail.value = cottage
    }

    fun onCottageDetailNavigated() {
        _navigateToCottageDetail.value = null
        searchQuery.value = ""
    }

    fun showKeyboard() {
       _isSearchBarFocused.value = true
    }

    fun closeKeyboard() {
        _isSearchBarFocused.value = false
    }

    override fun onCleared() {
        super.onCleared()
        dataSource.removeListener()
    }
}
