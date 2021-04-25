package fi.oamk.cottagerepublic.ui.cottageCreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.data.Cottage


class CreateCottageViewModelFactory(private val cottage: Cottage?) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCottageViewModel::class.java)) {
            return CreateCottageViewModel(cottage) as T
        }
        throw IllegalArgumentException("Unknown Class")
    }

}