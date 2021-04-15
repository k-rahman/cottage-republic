package fi.oamk.cottagerepublic.ui.bookingDetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.data.Cottage

class BookingDetailViewModelFactory(
    private val application: Application,
    private val selectedCottage: Cottage,
    private val selectedDates: List<String>
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingDetailViewModel::class.java))
            return BookingDetailViewModel(application, selectedCottage, selectedDates) as T

        throw IllegalArgumentException("Unknown Class")
    }
}