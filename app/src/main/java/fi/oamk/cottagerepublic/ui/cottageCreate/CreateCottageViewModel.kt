package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import android.util.Log
import android.util.MutableBoolean
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
    val newCottageLocationLat = MutableLiveData<String>()
    val newCottageLocationLon = MutableLiveData<String>()

    val newCottageAmenityList: MutableMap<String,Boolean> =  mutableMapOf<String,Boolean>(
        "Sauna" to false,
        "Pets" to false,
        "Power" to false,
        "Hottub" to false,
        "Smoking" to false,
        "Water" to false
        )
    val newCottageAmenities: MutableList<String> = mutableListOf()





    fun createCottage()
    {
        createAmenitiesList()
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!.toInt()
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location[newCottageCountry.value.toString()]=newCottageLocation.value.toString()
        newCottage.amenities=newCottageAmenities

        Log.v("Cottage: ",newCottage.toString())
        Log.v("Amenities: ",newCottageAmenityList.toString())
        //cottageDataSource.createNewCottage(newCottage)
    }

    fun createAmenitiesList()
    {

        for(amenity in newCottageAmenityList)
        {
            Log.v("Amenity: ", amenity.toString())
            if(amenity.value == true)
            {
                newCottageAmenities.add(amenity.key)
            }
        }
    }

    fun saunaCheck(checked : Boolean)
    {
        newCottageAmenityList["Sauna"]= checked
    }
    fun waterCheck(checked : Boolean)
    {
        newCottageAmenityList["Water"] = checked
    }
    fun powerCheck(checked : Boolean)
    {
        newCottageAmenityList["power"] = checked
    }
    fun petsCheck(checked : Boolean)
    {
        newCottageAmenityList["Pets"] = checked
    }
    fun smokingCheck(checked : Boolean)
    {
        Log.v("checkprint", checked.toString())
        newCottageAmenityList["Smoking"] = checked
    }
    fun hotTubCheck(checked : Boolean)
    {
        Log.v("checkprint", checked.toString())
        newCottageAmenityList["Hottub"] = checked
    }


}


