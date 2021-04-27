package fi.oamk.cottagerepublic.ui.account

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository
import kotlinx.coroutines.Dispatchers.IO

class AccountScreenViewModel() : ViewModel() {
    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())

    var email = MutableLiveData<String>("")
    var fullName = MutableLiveData<String>("")
   // var lastName = MutableLiveData<String>("")

    val userId = authDataSource.getCurrentUserId()


    val user = liveData(IO) {
        if (userId.isNotEmpty()) {
            val userDataResolved = userDataSource.getUserData(userId)
            emit(userDataResolved)
        }
    }

    fun setUserData(user: User) {
        email.value = user.email
        fullName.value = "${user.firstName} ${user.lastName}"
       // lastName.value = user.lastName
    }


}