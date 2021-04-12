package fi.oamk.cottagerepublic.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository

class HomeViewModel : ViewModel() {

    // The data source this ViewModel will fetch results from.
    private val dataSource = CottageRepository.getInstance(Firebase.database.getReference("cottages"))

    // This is private to avoid exposing a way to set this value to observers.
    private val _popularCottages = dataSource.getPopularCottages(3)

    // Views should use this to get access to the data.
    val popularCottages: LiveData<MutableList<Cottage>>
        get() = _popularCottages

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToCottageDetail = MutableLiveData<Cottage>()
    val navigateToCottageDetail: LiveData<Cottage>
        get() = _navigateToCottageDetail

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    // popular cottage item clickHandler
    fun onPopularCottageClicked(cottage: Cottage) {
        _navigateToCottageDetail.value = cottage
    }

    // Reset navigation trigger value
    fun onCottageDetailNavigated() {
        _navigateToCottageDetail.value = null
    }

    // search textView clickHandler
    fun onSearchClicked() {
        _navigateToSearch.value = true
    }

    // Reset navigation trigger value
    fun onSearchNavigated() {
        _navigateToSearch.value = false
    }
}