package fi.oamk.cottagerepublic.ui.auth


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.repository.AuthRepository

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppAuthRepository: AuthRepository
    val userLiveData: MutableLiveData<FirebaseUser>

    fun login(email: String?, password: String?) {
        authAppAuthRepository.login(email, password)
    }

    fun register(email: String?, password: String?) {
        authAppAuthRepository.register(email, password)
    }

    init {
        authAppAuthRepository = AuthRepository(application)
        userLiveData = authAppAuthRepository.getUserLiveData()
    }
}