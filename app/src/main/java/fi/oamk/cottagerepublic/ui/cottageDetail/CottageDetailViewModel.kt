package fi.oamk.cottagerepublic.ui.cottageDetail

import android.location.Address
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.repository.ReservationRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers.IO
import java.util.*

class CottageDetailViewModel(cottage: Cottage) : ViewModel() {
    private val reservationDataSource = ReservationRepository.getInstance(Firebase.database.reference)
    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))

    private val _selectedCottage = MutableLiveData<Cottage>()
    val selectedCottage: LiveData<Cottage>
        get() = _selectedCottage

    private var _host = MutableLiveData<User>()
    val host: LiveData<User>
        get() = _host

    init {
        _selectedCottage.value = cottage
        _host.value = User() // initialize user to an empty object till data comes back from database
    }

    val hostData = liveData(IO) {
        emit(userDataSource.getHostDataById(cottage.hostId))
    }

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

    var selectedDates = ObservableField<List<Date>>()
    var numberOfNights = ObservableField(0)

    val address = ObservableField<Address?>()

    val cottageReservations = liveData(IO) {
        emit(Resource.Loading<Boolean>())
        try {
            val reservationList = reservationDataSource.getReservationsByCottageId(selectedCottage.value!!.cottageId)
            emit(reservationList)
        } catch (e: Exception) {
            emit(Resource.Failure<Exception>(e.message!!))
        }
    }

    fun setHost(host: User) {
        _host.value = host
    }

    fun calendarShow() {
        _showCalendar.value = true
    }

    fun calendarHide() {
        _showCalendar.value = false
    }

    fun setAddress(address: Address) {
        this.address.set(address)
    }

    fun onMapClicked() {
        _navigateToMap.value = true
    }

    fun onMapNavigated() {
        _navigateToMap.value = false
    }

    fun onBookClicked() {
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