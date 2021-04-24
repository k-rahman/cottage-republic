package fi.oamk.cottagerepublic.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers

class MyCottagesViewModel: ViewModel() {

    // The data source this ViewModel will fetch results from.
    private val dataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )
    private val userDataSource = UserRepository(Firebase.database.getReference("users"))

    // populate myCottages live data when the getPopularCottage function return
    // using liveData builder https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    val myCottagesList = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val userId = userDataSource.getCurrentUserId()
            val cottageOwnersCottageKeys = userDataSource.getCottagesKeysByHostId(userId)
            val cottages = dataSource.getAllCottagesByCottagesKeys(cottageOwnersCottageKeys)
            emit(cottages)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    // When this variable value change, it will trigger navigation to MyCottage Form Screen
    private val _navigateToMyCottage = MutableLiveData<Cottage?>()
    val navigateToMyCottage: LiveData<Cottage?>
        get() = _navigateToMyCottage


    // my cottage item clickHandler
    fun onMyCottageClicked(cottage: Cottage) {
        // I want to navigate to the form that creates cottages but with it pre filled
        // with all the users cottage details that is ready for editing
        // so this function should set the navigation  value to be the cottage I need
        Log.i("MyCottageViewModel", "$cottage")
        _navigateToMyCottage.value = cottage

    }


    fun onCottageNavigated() {
        _navigateToMyCottage.value = null
    }



}