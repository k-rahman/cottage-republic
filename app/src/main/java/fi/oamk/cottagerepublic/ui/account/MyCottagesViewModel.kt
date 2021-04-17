package fi.oamk.cottagerepublic.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers

class MyCottagesViewModel : ViewModel() {

    // The data source this ViewModel will fetch results from.
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )

    // populate myCottages live data when the getPopularCottage function return
    // using liveData builder https://developer.android.com/topic/libraries/architecture/coroutines#livedata
    val myCottages = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val ownerCottagesList = cottageDataSource.getPopularCottages(3)
            emit(ownerCottagesList)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.cause!!))
        }
    }
}