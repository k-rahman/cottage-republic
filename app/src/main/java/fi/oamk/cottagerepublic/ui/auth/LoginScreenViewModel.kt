package fi.oamk.cottagerepublic.ui.auth

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import okhttp3.Dispatcher
import java.lang.Exception


class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository()
    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate


    var userIsLoggedIn = MutableLiveData<Boolean>()
    val loginUser = MutableLiveData<FirebaseUser>()

    init {
        userIsLoggedIn.value = authRepository.getLoggedOutLiveData().value
    }


//    fun onLoginClick(username: String?, password: String?) {
//        this.username.value = username
//        this.password.value = password
//        if (username.isNullOrBlank() || password.isNullOrBlank()) {
//            fillInBoxes()
//        } else {
//            login(use)
//            Log.v("Test", "login has been clicked")
//        }
//            authRepository.login(username, password)
//            if (authRepository.getUserLiveData().value != null) {
//                loginUser.value = authRepository.getUserLiveData().value
//                user = loginUser
//            } else {
//                loginUser.value = null
//            }
//            _navigate.value = true
//            _navigateToAccount.value = true
//    }

    fun login(username: String?, password: String?): LiveData<Resource<Any>> {
        return liveData(Dispatchers.IO) {
            try {
                val auth = authRepository.login(username, password)
                userIsLoggedIn = authRepository.getLoggedOutLiveData()
                emit(auth)
            } catch (e: Exception) {
                emit(Resource.Failure<Exception>(e.cause!!))
            }
        }
    }

    fun onLogoutClick() {
        authRepository.logOut()
        userIsLoggedIn = authRepository.getLoggedOutLiveData()
        _navigateToHome.value = true

    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }

    fun fillInBoxes() {
        Toast.makeText(
            getApplication(),
            "Please fill in all boxes",
            Toast.LENGTH_SHORT
        ).show()
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
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    // the register button functions
    fun onRegisterClick() {
        _navigateToRegister.value = true
    }

    fun onRegisterNavigated() {
        _navigateToRegister.value = false

    }

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    private val _navigateToAccount = MutableLiveData<Boolean>()
    val navigateToAccount: LiveData<Boolean>
        get() = _navigateToAccount

    fun onNavigatedToAccount() {
        _navigateToAccount.value = false
    }

}