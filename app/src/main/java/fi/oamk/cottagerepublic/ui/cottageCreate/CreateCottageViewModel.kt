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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {


    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )

    val amountOfGuests = MutableLiveData(listOf("1 Guest","2 Guests", "3 Guests","4 Guests"))
    val numberOfGuests = MutableLiveData("")

    val newCottageTitle = MutableLiveData<String>()
    val newCottageLocation = MutableLiveData<String>()
    val newCottageCountry = MutableLiveData<String>()
    val newCottageDescription = MutableLiveData<String>()
    val newCottagePrice = MutableLiveData<Int>()
    val newCottageGuests = MutableLiveData<String>()
    val newCottageLocationLat = MutableLiveData<String>()
    val newCottageLocationLon = MutableLiveData<String>()
    val newCottageAmenities = MutableLiveData<BooleanArray>()
//    val amountOfGuests = ObservableField<List<Int>>(listOf(1,2,3,4,5,6,7,8,9))
//    val numberOfGuests = ObservableField<Int>()


    fun createCottage()
    {
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!.toInt()
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location[newCottageCountry.value.toString()]=newCottageLocation.value.toString()
        Log.v("Cottage: ",newCottage.toString())
        cottageDataSource.createNewCottage(newCottage)
    }

}


