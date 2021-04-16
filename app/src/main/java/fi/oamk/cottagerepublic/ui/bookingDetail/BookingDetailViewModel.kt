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
    private val userDataSource = UserRepository()

    lateinit var checkIn: String
    lateinit var checkOut: String

    val numberOfNights = selectedDates.size - 1

    private val _navigateToSuccess = MutableLiveData<Boolean>()
    val navigateToSuccess: LiveData<Boolean>
        get() = _navigateToSuccess

    private val _navigateToCottageDetail = MutableLiveData<Boolean>()
    val navigateToCottageDetail: LiveData<Boolean>
        get() = _navigateToCottageDetail

    private val taxesPercentage = 21

    var totalBeforeTaxes = 0
        private set

    var totalAfterTaxes = 0
        private set

    var taxesAmount = 0
        private set

    init {
        formatDates()
        calculateTotalBeforeTaxes()
        calculateTaxesAmount()
        calculateTotalAfterTaxes()
    }

    private fun formatDates() {
        val formatDates = SimpleDateFormat("dd-MM-yyyy")
        val parsedCheckIn = formatDates.parse(selectedDates.first())
        val parsedCheckOut = formatDates.parse(selectedDates.last())

        formatDates.applyPattern("d. MMM")

        checkIn = formatDates.format(parsedCheckIn!!)
        checkOut = formatDates.format(parsedCheckOut!!)
    }

    private fun calculateTotalBeforeTaxes() {
        totalBeforeTaxes = selectedCottage.price * numberOfNights
    }

    private fun calculateTaxesAmount() {
        taxesAmount = (totalBeforeTaxes * taxesPercentage) / 100
    }

    private fun calculateTotalAfterTaxes() {
        totalAfterTaxes = totalBeforeTaxes + taxesAmount
    }

    fun onContinueClicked() {
        // if user doesn't have an id
        // redirect to login

        val userId = userDataSource.getCurrentUserId()
        reservationDataSource.createReservation(userId, selectedCottage.cottageId, selectedDates)
        _navigateToSuccess.value = true
    }

    fun onSucessNavigated() {
        _navigateToSuccess.value = false
    }

    fun onCancelClicked() {
        _navigateToCottageDetail.value = true
    }

    fun onCottageDetailNavigated() {
        _navigateToCottageDetail.value = false
    }
}