package fi.oamk.cottagerepublic.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers.IO

class MyCottagesViewModel: ViewModel() {

    // The data source this ViewModel will fetch results from.
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )
    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())
    val userId = authDataSource.getCurrentUserId()

    private var cottagesList = mutableListOf<Cottage>()

    // populate myCottages live data when the getPopularCottage function return
    // using liveData builder https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    val myCottagesList = liveData(IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val cottageOwnersCottageKeys = userDataSource.getCottagesKeysByHostId(userId)
            val cottages = cottageDataSource.getCottageByKey(cottageOwnersCottageKeys)
            cottagesList = (cottages as Resource.Success<MutableList<Cottage>>).data
            emit(cottages)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    // When this variable value change, it will trigger navigation to MyCottage Form Screen
    private val _navigateToMyCottage = MutableLiveData<Cottage?>()
    val navigateToMyCottage: LiveData<Cottage?>
        get() = _navigateToMyCottage

    private val _deleteCottageConfirmed = MutableLiveData<Boolean>()
    val deleteCottageConfirmed: LiveData<Boolean>
        get() = _deleteCottageConfirmed


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

    fun deleteCottageFromList(cottageId: String): LiveData<List<Cottage>> {
//        val userId = userDataSource.getCurrentUserId()
//        firebaseData
//            .child("users")
//            .child(userId)
//            .child("cottages")
//            .child(cottageId)
//            .removeValue()
//        firebaseData.child("cottages").child(cottageId).removeValue()
        return liveData(IO) {
            userDataSource.deleteCottageIdByHostId(userId, cottageId)
            cottageDataSource.deleteCottageById(cottageId)
            cottagesList.removeIf {
                it.cottageId == cottageId
            }
            emit(cottagesList)
        }

//        Log.i("MyCottageViewModel", cottageId)
    }

}