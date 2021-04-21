package fi.oamk.cottagerepublic.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers

class SearchViewModel(val cottage: Cottage?, val destination: Destination?) : ViewModel() {

    private val dataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )

    var cottagesList = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val cottages = dataSource.getAllCottages()
            emit(cottages)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    val searchQuery = MutableLiveData<String>()

    private val _navigateToCottageDetail = MutableLiveData<Cottage?>()
    val navigateToCottageDetail: LiveData<Cottage?>
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
            searchQuery.value = "${destination.location["city"]}, ${destination.location["country"]}"
            return true
        }

        if (cottage != null) {
            searchQuery.value = "${cottage.location["city"]}, ${cottage.location["country"]}"
            return true
        }

        return false
    }
}
