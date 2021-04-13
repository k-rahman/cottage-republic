package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.data.Cottage

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {

    private val _createCottage = MutableLiveData<Cottage>()
    val selectedCottage: LiveData<Cottage>
        get() = _createCottage


}