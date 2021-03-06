package fi.oamk.cottagerepublic.ui.cottageCreate

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.CottageRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CreateCottageViewModel(val cottage: Cottage?) : ViewModel() {

    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())

    //database reference
    private val cottageDataSource =
        CottageRepository.getInstance(
            Firebase.database.reference,
            Firebase.storage.reference
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
    var newCottageAmenities = mutableListOf<String>()

    // Variables set to init editable values
    var sauna = false
    var water = false
    var hottub = false
    var power = false
    var pets = false
    var smoking = false
    var fireplace = false
    var kitchen = false
    var boat = false
    var grill = false


    init {
        // Init cottage with editable values
        if (cottage != null) {
            newCottageTitle.value = cottage.cottageLabel
            newCottageLocation.value = cottage.location["city"]
            newCottageCountry.value = cottage.location["country"]
            newCottageDescription.value = cottage.description
            newCottageImageNames = cottage.images as ArrayList<String>
            cottageCoordinates = cottage.coordinates
            newCottagePrice.value = cottage.price.toString()
            numberOfGuests.value = cottage.guests
            for (amenity in cottage.amenities) {
                if (amenity == "sauna")
                    sauna = true
                if (amenity == "water")
                    water = true
                if (amenity == "hottub")
                    hottub = true
                if (amenity == "power")
                    power = true
                if (amenity == "pets")
                    pets = true
                if (amenity == "smoking")
                    smoking = true
                if (amenity == "grill")
                    grill = true
                if (amenity == "kitchen")
                    kitchen = true
                if (amenity == "boat")
                    boat = true
                if (amenity == "fireplace")
                    fireplace = true
            }
        }
    }

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

    //variable for fragment, in case of missing fields
    var fillInBoxes = MutableLiveData<List<String>>()

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    //navigation variables
    private var _navigateContinue = MutableLiveData<Boolean>()
    val navigateContinue: LiveData<Boolean>
        get() = _navigateContinue

    private var _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap

    // When this variable value change, it will trigger navigation to MyCottage Form Screen
    private val _navigateToMyCottage = MutableLiveData<Boolean>()
    val navigateToMyCottage: LiveData<Boolean>
        get() = _navigateToMyCottage

    fun onCreateCottageClicked() {
        if (checkFields().isNotEmpty())
            fillInBoxes.value = checkFields()
        else {
            showLoading()
            createCottage()
        }
    }

    //create cottage function, sends cottage object to db through cottagerepo
    private fun createCottage() {
        val newCottage = Cottage()
        newCottage.guests = numberOfGuests.value!!
        newCottage.rating = ((0..5).random()).toFloat()
        newCottage.cottageLabel = newCottageTitle.value.toString()
        newCottage.description = newCottageDescription.value.toString()
        newCottage.location["city"] = newCottageLocation.value.toString()
        newCottage.location["country"] = newCottageCountry.value.toString()
        newCottage.amenities = newCottageAmenities
        newCottage.hostId = authDataSource.getCurrentUserId()!!
        if (newCottagePrice.value != "")
            newCottage.price = newCottagePrice.value!!.toInt()
        else
            newCottage.price = 0
        newCottage.coordinates = cottageCoordinates

        if (cottage != null) {
            // update cottage
            CoroutineScope(Main).launch {
                newCottage.cottageId = cottage.cottageId
                newCottage.images = editImages(newCottageImages, cottage.images)
                cottageDataSource.updateCottageByCottageId(newCottage)
                hideLoading()
                navigateToMyCottages()
            }
        } else {
            //create new cottage
            CoroutineScope(Main).launch {
                newCottage.images = addImages(newCottageImages)
                val key = cottageDataSource.createNewCottage(newCottage)
                userDataSource.pushCottageToUser(newCottage.hostId, key)
                hideLoading()
                navigateToMyCottages()
            }
        }
    }

    private suspend fun addImages(newCottageImages: ArrayList<Uri>): ArrayList<String> {
        cottageDataSource.uploadImages(newCottageImages)
        return cottageDataSource.getImagesUrl(newCottageImages)
    }

    private suspend fun editImages(
        newCottageImages: ArrayList<Uri>,
        oldImagesUrl: MutableList<String>
    ): MutableList<String> {
        val newImagesUrl = addImages(newCottageImages)

        return if (oldImagesUrl.size > newImagesUrl.size) {
            //replace images
            for (i in 0 until newImagesUrl.size) {
                oldImagesUrl.removeAt(i)
                oldImagesUrl.add(i, newImagesUrl[i])
            }
            oldImagesUrl
        } else
            newImagesUrl
    }

    //check if user has filled in all the required fields
    private fun checkFields(): MutableList<String> {
        val checkTheseFields = mutableListOf<String>()

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
        return !newCottageImages.isNullOrEmpty()
    }

    private fun navigateToMyCottages() {
        _navigateToMyCottage.value = true
    }

    private fun showLoading() {
        _loading.value = true
    }

    private fun hideLoading() {
        _loading.value = false
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

    fun onMyCottagesNavigated() {
        _navigateToMyCottage.value = false
    }

    fun onMapClicked() {
        _navigateToMap.value = true
    }

    fun onMapNavigated() {
        _navigateToMap.value = false
    }

    fun setAddress(newAddress: String) {
        newCottageAddress.value = newAddress
    }
}