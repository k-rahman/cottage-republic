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
    val newCottagePrice = MutableLiveData<String>("0")
    val newCottageLocationLat = MutableLiveData<String>()
    var cottageCoordinates = hashMapOf<String,Double>()

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap

//    val newCottageAmenityList: MutableMap<String,Boolean> =  mutableMapOf<String,Boolean>(
//        "sauna" to false,
//        "pets" to false,
//        "power" to false,
//        "hottub" to false,
//        "smoking" to false,
//        "water" to false
//        )

    val newCottageAmenities: MutableList<String> = mutableListOf()


    fun createCottage()
    {
//        createAmenitiesList()

        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!.toInt()
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location[newCottageCountry.value.toString()]=newCottageLocation.value.toString()
        newCottage.amenities=newCottageAmenities
        newCottage.price = newCottagePrice?.value!!.toInt()


        //create new cottage
        cottageDataSource.createNewCottage(newCottage)

    //debug
//        Log.v("Cottage: ",newCottage.toString())
//        Log.v("Amenities: ",newCottageAmenityList.toString())

    }

//    fun createAmenitiesList()
//    {
//
//        for(amenity in newCottageAmenityList)
//        {
//            Log.v("Amenity: ", amenity.toString())
//            if(amenity.value)
//            {
//                newCottageAmenities.add(amenity.key)
//            }
//        }
//    }

    fun saunaCheck(checked : Boolean)
    {


        if(checked)
            newCottageAmenities.add("sauna")
        else
            newCottageAmenities.remove("sauna")

     //   newCottageAmenityList["sauna"]= checked
    }
    fun waterCheck(checked : Boolean)
    {

        if(checked)
            newCottageAmenities.add("water")
        else
            newCottageAmenities.remove("water")

    //    newCottageAmenityList["water"] = checked
    }
    fun powerCheck(checked : Boolean)
    {
        if(checked)
            newCottageAmenities.add("power")
        else
            newCottageAmenities.remove("power")

       // newCottageAmenityList["power"] = checked
    }
    fun petsCheck(checked : Boolean)
    {

        if(checked)
            newCottageAmenities.add("pets")
        else
            newCottageAmenities.remove("pets")

    //    newCottageAmenityList["pets"] = checked
    }
    fun smokingCheck(checked : Boolean)
    {

        if(checked)
            newCottageAmenities.add("smoking")
        else
            newCottageAmenities.remove("smoking")

   //     newCottageAmenityList["smoking"] = checked
    }
    fun hotTubCheck(checked : Boolean)
    {

        if(checked)
            newCottageAmenities.add("huttub")
        else
            newCottageAmenities.remove("hottub")

   //     newCottageAmenityList["hottub"] = checked
    }

    fun onMapClicked() {
        _navigateToMap.value = true
    }

    fun onMapNavigated() {
        _navigateToMap.value = false
    }
}


