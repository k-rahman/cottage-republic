package fi.oamk.cottagerepublic.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginScreenViewModel : ViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate
//    val username: LiveData<String>
//        get() = _username

    fun onRegisterClick()
    {

    }

    fun onLoginClick(username: String , password: String)
    {
        //Call login function from repository
        //if response successful

        _navigate.value = true


    }

    fun doneNavigate()
    {
        _navigate.value = false
    }

}