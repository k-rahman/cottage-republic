package fi.oamk.cottagerepublic.ui.bookingDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.ReservationRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import java.text.SimpleDateFormat

class BookingDetailViewModel(
    application: Application,
    val selectedCottage: Cottage,
    private val selectedDates: List<String>
) :
    AndroidViewModel(application) {
    private val reservationDataSource = ReservationRepository(Firebase.database.reference)
    private val userDataSource = UserRepository(Firebase.database.getReference("users"))

    lateinit var checkIn: String
    lateinit var checkOut: String

    val numberOfNights = selectedDates.size - 1

    private val _navigateToSuccess = MutableLiveData<Boolean>()
    val navigateToSuccess: LiveData<Boolean>
        get() = _navigateToSuccess

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    private val taxesPercentage = 21

    var total= 0
        private set

    var taxesAmount = 0
        private set

    init {
        formatDates()
        calculateTotal()
        calculateTaxesAmount()
    }

    private fun formatDates() {
        val formatDates = SimpleDateFormat("dd-MM-yyyy")
        val parsedCheckIn = formatDates.parse(selectedDates.first())
        val parsedCheckOut = formatDates.parse(selectedDates.last())

        formatDates.applyPattern("d. MMM")

        checkIn = formatDates.format(parsedCheckIn!!)
        checkOut = formatDates.format(parsedCheckOut!!)
    }

    private fun calculateTotal() {
        total = selectedCottage.price * numberOfNights
    }

    private fun calculateTaxesAmount() {
        taxesAmount = (total * taxesPercentage) / 100
    }

    fun onConfirmClicked() {
        val userId = userDataSource.getCurrentUserId()
        reservationDataSource.createReservation(userId, selectedCottage.cottageId, selectedDates)
        _navigateToSuccess.value = true
    }

    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    fun onLoginNavigated() {
        _navigateToLogin.value = false
    }

    fun onRegisterClicked() {
        _navigateToRegister.value = true
    }

    fun onRegisterNavigated() {
        _navigateToRegister.value = false
    }

    fun onSucessNavigated() {
        _navigateToSuccess.value = false
    }
}