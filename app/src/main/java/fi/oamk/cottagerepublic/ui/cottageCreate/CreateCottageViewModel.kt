package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {

    //database reference
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.getReference("cottages"),
            Firebase.storage.getReference("cottages")
        )

    //all the values that get pushed to new cottage
    val amountOfGuests = MutableLiveData(listOf("1 Guest","2 Guests", "3 Guests","4 Guests", "5 Guests", "6+ Guests"))
    val numberOfGuests = MutableLiveData("")

    val newCottageTitle = MutableLiveData<String>()
    val newCottageLocation = MutableLiveData<String>()
    val newCottageCountry = MutableLiveData<String>()
    val newCottageDescription = MutableLiveData<String>()
    var newCottagePrice = MutableLiveData<String>("0")
    var cottageCoordinates = hashMapOf<String,Double>()

    var newCottageImages = arrayListOf<Uri>()
    var newCottageImageNames = arrayListOf<String>()

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap


    val newCottageAmenities: MutableList<String> = mutableListOf()


    //create cottage function, sends cottage object to db through cottagerepo
    fun createCottage()
    {
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!.toInt()
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location[newCottageCountry.value.toString()]=newCottageLocation.value.toString()
        newCottage.amenities=newCottageAmenities
        if (newCottagePrice.value != "")
             newCottage.price = newCottagePrice.value!!.toInt()
        else
            newCottage.price = 0
        newCottage.coordinates = cottageCoordinates
        newCottage.images =  newCottageImageNames
        //create new cottage
        cottageDataSource.createNewCottage(newCottage,newCottageImages)


    }

    //check if user has filled in all the required fields




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


