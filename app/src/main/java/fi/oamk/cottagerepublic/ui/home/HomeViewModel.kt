package fi.oamk.cottagerepublic.ui.home

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
import fi.oamk.cottagerepublic.repository.DestinationRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers

class HomeViewModel : ViewModel() {
    // The data source this ViewModel will fetch results from.
    private val destinationDataSource =
        DestinationRepository.getInstance(
            Firebase.database.getReference("destinations"),
            Firebase.storage.getReference("destinations")
        )
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )

    // populate popularCottages live data when the getPopularCottage function return
    // using liveData builder https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    val popularCottages = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val popularCottagesList = cottageDataSource.getPopularCottages(3)
            emit(popularCottagesList)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    val popularDestinations = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val popularDestinations = destinationDataSource.getPopularDestinations()
            emit(popularDestinations)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearchCottage = MutableLiveData<Cottage?>()
    val navigateToSearchCottage: LiveData<Cottage?>
        get() = _navigateToSearchCottage

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearchDestination = MutableLiveData<Destination?>()
    val navigateToSearchDestination: LiveData<Destination?>
        get() = _navigateToSearchDestination

    // When this variable value change, it will trigger navigation to Cottage Detail Screen
    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    // popular cottage item clickHandler
    fun onPopularCottageClicked(cottage: Cottage) {
        _navigateToSearchCottage.value = cottage
    }

    fun onPopularDestinationClicked(destination: Destination) {
        _navigateToSearchDestination.value = destination
    }

    // search textView clickHandler
    fun onSearchClicked() {
        _navigateToSearch.value = true
    }

    // Reset navigation trigger value
    fun onSearchNavigated() {
        _navigateToSearch.value = false
        _navigateToSearchDestination.value = null
        _navigateToSearchCottage.value = null
    }
}