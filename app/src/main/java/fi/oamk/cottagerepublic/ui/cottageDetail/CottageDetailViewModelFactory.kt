package fi.oamk.cottagerepublic.ui.cottageDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.data.Cottage
import java.lang.IllegalArgumentException

class CottageDetailViewModelFactory(val cottage: Cottage) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CottageDetailViewModel::class.java)) {
            return CottageDetailViewModel(cottage) as T
        }

        throw IllegalArgumentException("Unknown class")
    }
}