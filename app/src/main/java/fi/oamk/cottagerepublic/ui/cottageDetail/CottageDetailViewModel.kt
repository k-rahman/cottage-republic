package fi.oamk.cottagerepublic.ui.cottageDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.data.Cottage

class CottageDetailViewModel(cottage: Cottage) : ViewModel() {

    private val _selectedCottage = MutableLiveData<Cottage>()
    val selectedCottage: LiveData<Cottage>
        get() = _selectedCottage

    init {
        _selectedCottage.value = cottage
    }
}