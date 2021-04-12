package fi.oamk.cottagerepublic.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.repository.DestinationRepository

class HomeViewModel : ViewModel() {

    // The data source this ViewModel will fetch results from.
    private val cottagesDataSource = CottageRepository.getInstance(Firebase.database.getReference("cottages"))
    private val destinationDataSource = DestinationRepository.getInstance(Firebase.database.getReference("destinations"))

    // This is private to avoid exposing a way to set this value to observers.
    private val _popularCottages = cottagesDataSource.getPopularCottages(3)

    // Views should use this to get access to the data.
    val popularCottages: LiveData<MutableList<Cottage>>
        get() = _popularCottages

    private val _popularDestinations = destinationDataSource.getPopularDestinations()
    val popularDestinations: LiveData<MutableList<Destination>>
        get() = _popularDestinations

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToCottageDetail = MutableLiveData<Cottage>()
    val navigateToCottageDetail: LiveData<Cottage>
        get() = _navigateToCottageDetail

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearchDestination = MutableLiveData<Destination>()
    val navigateToSearchDestination: LiveData<Destination>
        get() = _navigateToSearchDestination

    // popular cottage item clickHandler
    fun onPopularCottageClicked(cottage: Cottage) {
        _navigateToCottageDetail.value = cottage
    }

    fun onPopularDestinationClicked(destination: Destination) {
        _navigateToSearchDestination.value = destination
    }

    fun onSearchDestinationNavigated() {
        _navigateToSearchDestination.value = null
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