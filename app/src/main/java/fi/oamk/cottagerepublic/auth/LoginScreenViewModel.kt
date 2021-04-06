package fi.oamk.cottagerepublic.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginScreenViewModel : ViewModel() {
    val username = MutableLiveData<String>()
//    val username: LiveData<String>
//        get() = _username

}