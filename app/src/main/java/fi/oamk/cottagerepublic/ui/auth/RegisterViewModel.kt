package fi.oamk.cottagerepublic.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())
    var userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
    var emailError = MutableLiveData<String?>()
    var passwordError = MutableLiveData<String?>()
    var confirmPasswordError = MutableLiveData<String?>()

    var isRegistered = MutableLiveData<Resource<Any>>()

    fun onRegisterClicked(email: String?, password: String?) {
        showEmailErrors()
        showPasswordErrors()
        showConfirmPasswordErrors()
        if (
            !emailError.value.isNullOrBlank() ||
            !passwordError.value.isNullOrBlank() ||
            !confirmPasswordError.value.isNullOrBlank()
        )
            return
        else
            register(email!!, password!!)
    }

    private fun register(email: String, password: String) {
        CoroutineScope(Main).launch {
            isRegistered.value = authDataSource.register(email, password)
        }
    }

    fun addUserEmailToDb(email: String) {
        userDataSource.addUserEmail(authDataSource.getCurrentUserId()!!, email)
    }

    fun showEmailErrors() {
        when {
            email.value.isNullOrBlank() ->
                setEmailEmptyError()
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value.toString()).matches() ->
                setEmailIncorrectError()
            else -> clearEmailError()
        }
    }

    fun showPasswordErrors() {
        when {
            password.value.isNullOrBlank() ->
                setPasswordEmptyError()
            password.value.toString().length < 6 ->
                setPasswordLengthError()
            else -> clearPasswordError()
        }
    }

    fun showConfirmPasswordErrors() {
        when {
            confirmPassword.value.toString() != password.value.toString() ->
                setPasswordMatchError()
            else -> clearConfirmPasswordError()
        }
    }

    private fun setEmailEmptyError() {
        emailError.value = "Email can not be empty"
    }

    private fun setEmailIncorrectError() {
        emailError.value = "Incorrect email format!"
    }

    private fun setPasswordEmptyError() {
        passwordError.value = "Password can not be empty"
    }

    private fun setPasswordLengthError() {
        passwordError.value = "Password must be at least 6 characters!"
    }

    private fun setPasswordMatchError() {
        confirmPasswordError.value = "Password doesn't match!"
    }

    private fun clearEmailError() {
        emailError.value = null
    }

    private fun clearPasswordError() {
        passwordError.value = null
    }

    private fun clearConfirmPasswordError() {
        confirmPasswordError.value = null
    }
}