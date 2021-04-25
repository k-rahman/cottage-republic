package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Application
import android.location.Address
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.repository.UserRepository

class CreateCottageViewModel(application: Application) : AndroidViewModel(application) {

    private val userDataSource = UserRepository(Firebase.database.getReference("users"))

    //database reference
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.reference,
            Firebase.storage.reference
        )

    //all the values that get pushed to new cottage
    val amountOfGuests = MutableLiveData(
        listOf(
            "1 Guest",
            "2 Guests",
            "3 Guests",
            "4 Guests",
            "5 Guests",
            "6+ Guests"
        )
    )
    val numberOfGuests = MutableLiveData("")


    val newCottageTitle = MutableLiveData<String>()
    val newCottageLocation = MutableLiveData<String>()
    val newCottageCountry = MutableLiveData<String>()
    val newCottageDescription = MutableLiveData<String>()
    var newCottagePrice = MutableLiveData("0")
    var cottageCoordinates = hashMapOf<String, Double>()

    var newCottageAddress = MutableLiveData<String>()

    var newCottageImages = arrayListOf<Uri>()
    var newCottageImageNames = arrayListOf<String>()

    //variable for fragment, in case of missing fields
    var fillInBoxes = MutableLiveData<List<String>>()

    //navigation variables
    private var _navigateContinue = MutableLiveData<Boolean>()
    val navigateContinue: LiveData<Boolean>
        get() = _navigateContinue

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap


    val newCottageAmenities = mutableListOf<String>()


    //create cottage function, sends cottage object to db through cottagerepo
    fun createCottage() {
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!.toInt()
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location["city"] = newCottageLocation.value.toString()
        newCottage.location["country"] = newCottageCountry.value.toString()
        newCottage.amenities = newCottageAmenities
        newCottage.hostId = userDataSource.getCurrentUserId()
        if (newCottagePrice.value != "")
            newCottage.price = newCottagePrice.value!!.toInt()
        else
            newCottage.price = 0
        newCottage.coordinates = cottageCoordinates
        newCottage.images = newCottageImageNames
        //create new cottage
        if (checkFields().isEmpty()) {
            val key = cottageDataSource.createNewCottage(newCottage, newCottageImages)
            userDataSource.pushCottageToUser(newCottage.hostId, key)
            onContinueClicked()
        } else
            fillInBoxes.value = checkFields()


    }

    //check if user has filled in all the required fields
    private fun checkFields(): MutableList<String> {
        var checkTheseFields = mutableListOf<String>()

        if (!checkTitle())
            checkTheseFields.add("Title")
        if (!checkPrice())
            checkTheseFields.add("Price")
        if (!checkLocations())
            checkTheseFields.add("City")
        if (!checkCountry())
            checkTheseFields.add("Country")
        if (!checkCoordinates())
            checkTheseFields.add("Coordinates")
        if (!checkImages())
            checkTheseFields.add("Images")

        return checkTheseFields
    }

    private fun checkTitle(): Boolean {
        return !newCottageTitle.value.isNullOrBlank()
    }

    private fun checkPrice(): Boolean {
        return !newCottagePrice.value.isNullOrBlank()
    }

    private fun checkLocations(): Boolean {
        return !newCottageLocation.value.isNullOrBlank()
    }

    private fun checkCountry(): Boolean {
        return !newCottageCountry.value.isNullOrBlank()
    }

    private fun checkCoordinates(): Boolean {
        return !cottageCoordinates.isNullOrEmpty()

    }

    private fun checkImages(): Boolean {
        return !newCottageImageNames.isNullOrEmpty()
    }


    fun saunaCheck(checked: Boolean) {


        if (checked)
            newCottageAmenities.add("sauna")
        else
            newCottageAmenities.remove("sauna")

    }

    fun waterCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("water")
        else
            newCottageAmenities.remove("water")

    }

    fun powerCheck(checked: Boolean) {
        if (checked)
            newCottageAmenities.add("power")
        else
            newCottageAmenities.remove("power")

    }

    fun petsCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("pets")
        else
            newCottageAmenities.remove("pets")

    }

    fun smokingCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("smoking")
        else
            newCottageAmenities.remove("smoking")

    }

    fun hotTubCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("hottub")
        else
            newCottageAmenities.remove("hottub")

    }

    fun kitchenCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("kitchen")
        else
            newCottageAmenities.remove("kitchen")

    }

    fun fireplaceCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("fireplace")
        else
            newCottageAmenities.remove("fireplace")

    }

    fun boatCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("boat")
        else
            newCottageAmenities.remove("boat")

    }

    fun grillCheck(checked: Boolean) {

        if (checked)
            newCottageAmenities.add("grill")
        else
            newCottageAmenities.remove("grill")

    }

    fun onMapClicked() {
        _navigateToMap.value = true
    }

    fun onMapNavigated() {
        _navigateToMap.value = false
    }

    fun onContinueClicked() {
        _navigateContinue.value = true
    }

    fun onContinueNavigated() {
        _navigateContinue.value = false
    }


    fun setAddress(newAddress: String) {
        newCottageAddress.value = newAddress
    }


}


