package fi.oamk.cottagerepublic.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.repository.AuthRepository


class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate



    fun onLoginClick(username: String?, password: String?) {
        if(username?.length != 0 && password?.length != 0) {
            authRepository.fillInBoxes()
        }
        else
        {
            authRepository?.login(username, password)
            _navigate.value = true
        }

        Log.v("Test", "login has been clicked")

    }

    fun onRegisterClick(username: String?, password: String?) {
        if(username?.length != 0 && password?.length != 0) {
            authRepository.fillInBoxes()
        }
        else {
            authRepository?.register(username, password)
        }
        Log.v("Test", "Registration has been clicked")

    }



    fun doneNavigate()
    {
        _navigate.value = false
    }

    init {
        authRepository = AuthRepository(application)
        userLiveData = authRepository.getUserLiveData()
    }

}