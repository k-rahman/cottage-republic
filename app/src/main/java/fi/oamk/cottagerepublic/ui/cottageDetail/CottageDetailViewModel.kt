package fi.oamk.cottagerepublic.ui.cottageDetail

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.ReservationRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers

class CottageDetailViewModel(cottage: Cottage) : ViewModel() {
    private val reservationDataSource = ReservationRepository.getInstance(Firebase.database.reference)

    val cottageReservations = liveData(Dispatchers.IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val reservationList = reservationDataSource.getReservationsByCottageId(selectedCottage.value!!.cottageId)
            emit(reservationList)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.cause!!))
        }
    }

    private val _selectedCottage = MutableLiveData<Cottage>()
    val selectedCottage: LiveData<Cottage>
        get() = _selectedCottage

    private var _showCalendar = MutableLiveData<Boolean>()
    val showCalendar: LiveData<Boolean>
        get() = _showCalendar

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap

    private var _navigateToBookingDetail = MutableLiveData<Boolean>()
    val navigateToBookingDetails: LiveData<Boolean>
        get() = _navigateToBookingDetail

    private var _launchEmail = MutableLiveData<Boolean>()
    val launchEmail: LiveData<Boolean>
        get() = _launchEmail

    var numberOfNights = ObservableField(0)

    init {
        _selectedCottage.value = cottage
    }

    fun calendarShow() {
        _showCalendar.value = true
    }

    fun calendarHide() {
        _showCalendar.value = false
    }

    fun onMapClicked() {
        _navigateToMap.value = true
    }

    fun onMapNavigated() {
        _navigateToMap.value = false
    }

    fun onBookClicked() {

        calendarHide()
        _navigateToBookingDetail.value = true
    }

    fun onBookingNavigated() {
        _navigateToBookingDetail.value = false
    }

    fun onAskHostClicked() {
        _launchEmail.value = true
    }

    fun onEmailLaunched() {
        _launchEmail.value = false
    }
}