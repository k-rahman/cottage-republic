package fi.oamk.cottagerepublic.ui.cottageDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.data.Cottage

class CottageDetailViewModel(cottage: Cottage) : ViewModel() {

    private val _selectedCottage = MutableLiveData<Cottage>()
    val selectedCottage: LiveData<Cottage>
        get() = _selectedCottage


    private var _showCalendar = MutableLiveData<Boolean>()
    val showCalendar: LiveData<Boolean>
        get() = _showCalendar

    init {
        _selectedCottage.value = cottage
    }

    fun calendarShow() {
        _showCalendar.value = true
    }

    fun calendarShowed() {
        _showCalendar.value = false
    }
}