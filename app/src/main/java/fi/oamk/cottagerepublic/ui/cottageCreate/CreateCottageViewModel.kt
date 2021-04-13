package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.data.Cottage

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {

    val newCottageTitle = MutableLiveData<String>()
    val newCottageLocation = MutableLiveData<String>()
    val newCottageDescription = MutableLiveData<String>()
    val newCottagePrice = MutableLiveData<Int>()
    val newCottageGuests = MutableLiveData<Int>()
    val newCottageLocationLat = MutableLiveData<String>()
    val newCottageLocationLon = MutableLiveData<String>()
    val newCottageAmenities = MutableLiveData<BooleanArray>()

}
