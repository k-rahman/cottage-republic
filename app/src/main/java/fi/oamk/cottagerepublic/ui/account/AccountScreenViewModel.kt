package fi.oamk.cottagerepublic.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.Dispatchers.IO

class AccountScreenViewModel : ViewModel() {
    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())

    val email = MutableLiveData("")
    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")

    fun getUserData(): LiveData<Resource<Any>> {
        val userId = authDataSource.getCurrentUserId()

        return liveData(IO) {
            if (userId != null) {
                val userDataResolved = userDataSource.getUserData(userId)
                emit(userDataResolved)
            }
        }
    }

    fun setUserData(user: User) {
        email.value = user.email
        firstName.value = user.firstName
        lastName.value = user.lastName
    }
}