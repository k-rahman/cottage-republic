package fi.oamk.cottagerepublic.ui.auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository.getInstance(FirebaseAuth.getInstance())
    private val userRepository: UserRepository = UserRepository(Firebase.database.getReference("users"))

    // The current username and password
    var username = ""
    var password = ""

    // Retrieve currentUser information
    var user: MutableLiveData<FirebaseUser> = authRepository.getCurrentUser()

    // Using MutableLiveData to observe if user is logged in or not
    var isLoggedIn = MutableLiveData<Resource<Any>>()

    private var _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun onLoginClick(username: String, password: String) {
        // isBlank() Returns `true` if this string is empty or consists solely of whitespace characters.
        if(username.isBlank() || password.isBlank()) {
            // if false error message is displayed
            showLoginErrorMessage()
        }
        else
        {
            login(username, password)
        }
    }


    private fun login(username: String, password: String) {
        // Main: Optimized for UI code or non-blocking code that executes fast
        CoroutineScope(Main).launch{
            // Coroutine will launch the suspend function and invoke login
            isLoggedIn.value = authRepository.login(username, password)
            // currentuser is now set
            user = authRepository.getCurrentUser()
        }
    }

    fun onLogoutClick() {
        // Log current user out from firebase
        authRepository.logOut()
        // reset currentuser
        user = authRepository.getCurrentUser()
        // navigate to homescreen
        _navigate.value = true
    }

    fun resetLogin() {
        // reset values
        isLoggedIn = MutableLiveData<Resource<Any>>()
        username = ""
        password = ""
    }

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


    fun onNavigated() {
        _navigate.value = false
    }

    private fun showLoginErrorMessage() {
        Toast.makeText(
            getApplication(),
            "Please enter email & password",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun showFailureErrorMessage(error: String) {
        Toast.makeText(
            getApplication(),
            error,
            Toast.LENGTH_SHORT
        ).show()
    }
}