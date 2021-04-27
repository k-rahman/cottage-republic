package fi.oamk.cottagerepublic.ui.account

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class AccountUserSettingsScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())
    private val userId = authDataSource.getCurrentUserId()

    val email = MutableLiveData("")
    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val phone = MutableLiveData("")
    var user: User? = null
    var saveStatus = MutableLiveData<Resource<Any>>()

    var loginFragment: Boolean? = null

    val userData = liveData(IO) {
        val data = userDataSource.getUserData(userId)
        emit(data)
    }

    fun onSaveClick() {
        if (firstName.value.toString().isBlank() || lastName.value.toString().isBlank() || phone.value.toString().isBlank()) {
            showEmptyFieldsError()

        } else {
            user?.firstName = firstName.value.toString()
            user?.lastName = lastName.value.toString()
            user?.phone = phone.value.toString()

            CoroutineScope(Main).launch {
                saveStatus.value = userDataSource.updateUserData(user!!)

                if (loginFragment != null)
                    _navigateToLogin.value = true
                else
                    _navigateToProfile.value = true
            }
        }
    }

    fun setUserData(user: User) {
        email.value = user.email
        firstName.value = user.firstName
        lastName.value = user.lastName
        phone.value = user.phone
        this.user = user
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