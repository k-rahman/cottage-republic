package fi.oamk.cottagerepublic.ui.account

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import fi.oamk.cottagerepublic.repository.UserRepository

class AccountUserSettingsScreenViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepository()
    var loading = MutableLiveData<Boolean>()
    var saveStatus = MutableLiveData<Boolean>()

    val email = MutableLiveData("email")
    val fname = MutableLiveData("fname")
    val lname = MutableLiveData("lname")
    val phone = MutableLiveData("phone")
    init {
        // will return true if get data and false if something goes wrong
       if (!userRepository.getCurrentUserData(email, fname, lname, phone)){
           loading.value =false
       }

    }

    fun onSaveClick (){
        Log.i("saveClick", "works")
        saveStatus.value = (userRepository.updateUserData(fname.value.toString(), lname.value.toString(), phone.value.toString()))
    }
    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile:LiveData<Boolean>
    get()= _navigateToProfile
    fun onCancelClick (){
        Log.i("CancelClick", "works")
            _navigateToProfile.value = true
    }
    fun onProfileNavigated(){
        _navigateToProfile.value = false
    }


}