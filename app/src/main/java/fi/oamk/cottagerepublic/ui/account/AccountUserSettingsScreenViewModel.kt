package fi.oamk.cottagerepublic.ui.account

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository

class AccountUserSettingsScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())
    private val userId = authDataSource.getCurrentUserId()

    var loading = MutableLiveData<Boolean>()
    var saveStatus = MutableLiveData<Boolean>()

    val email = MutableLiveData("")
    val fname = MutableLiveData("")
    val lname = MutableLiveData("")
    val phone = MutableLiveData("")

    var loginFragment: Boolean? = null

    init {
        // will return true if get data and false if something goes wrong
        userDataSource.getCurrentUserData(userId, email, fname, lname, phone)
    }

    fun onSaveClick() {
        if (fname.value.toString().isBlank() || lname.value.toString().isBlank() || phone.value.toString().isBlank()) {
            showEmptyFieldsError()

        } else {
            userDataSource.updateUserData(
                userId,
                fname.value.toString(),
                lname.value.toString(),
                phone.value.toString()
            )
            if (loginFragment != null)
                _navigateToLogin.value = true
            else
                _navigateToProfile.value = true
        }
    }

    private fun showEmptyFieldsError() {
        Toast.makeText(getApplication(), "All fields are required", Toast.LENGTH_SHORT).show()
    }

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean>
        get() = _navigateToProfile

    fun onLoginNavigated() {
       _navigateToLogin.value = false
        loginFragment = null
    }

    fun onProfileNavigated() {
        _navigateToProfile.value = false
    }


}