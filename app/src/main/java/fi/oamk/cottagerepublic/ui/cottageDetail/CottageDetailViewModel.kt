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

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap

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
}