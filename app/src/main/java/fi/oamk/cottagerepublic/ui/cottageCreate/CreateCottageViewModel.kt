package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.data.Cottage

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {


    val newCottageTitle = MutableLiveData<String>()
    val newCottageLocation = MutableLiveData<String>()
    val newCottageCountry = MutableLiveData<String>()
    val newCottageDescription = MutableLiveData<String>()
    val newCottagePrice = MutableLiveData<Int>()
    val newCottageGuests = MutableLiveData<String>()
    val newCottageLocationLat = MutableLiveData<String>()
    val newCottageLocationLon = MutableLiveData<String>()
    val newCottageAmenities = MutableLiveData<BooleanArray>()


    val amountOfGuests = ObservableField<List<Int>>(listOf(1,2,3,4,5))
    val numberOfGuests = ObservableField<Int>()



    fun createCottage()
    {
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.get()!!
    }

}


