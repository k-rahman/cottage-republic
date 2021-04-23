package fi.oamk.cottagerepublic.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fi.oamk.cottagerepublic.repository.AuthRepository


class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(
    )
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate



    fun onLoginClick(username: String?, password: String?) {
        if(username.isNullOrBlank() || password.isNullOrBlank()) {
        //    authRepository.fillInBoxes()
        }
        else
        {
            authRepository.login(username, password)
            _navigate.value = true
        }

        Log.v("Test", "login has been clicked")

    }

   /*
    fun onRegisterClick(username: String?, password: String?) {
        if(username.isNullOrBlank() || password.isNullOrBlank()) {
            authRepository.fillInBoxes()
        }
        else {
            authRepository?.register(username, password)
        }
        Log.v("Test", "Registration has been clicked")
    }
*/
    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister:LiveData<Boolean>
    get() =  _navigateToRegister
    // the register button functions
        fun onRegisterClick(){
           _navigateToRegister.value = true
       }
        fun onRegisterNavigated() {
           _navigateToRegister.value = false

       }
//        fun doneNavigate() {
//        _navigate.value = false
//        }
}