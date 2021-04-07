package fi.oamk.cottagerepublic.auth


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.repository.RepositoryModel

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authAppRepository: RepositoryModel
    val userLiveData: MutableLiveData<FirebaseUser>

    fun login(email: String?, password: String?) {
        authAppRepository.login(email, password)
    }

    fun register(email: String?, password: String?) {
        authAppRepository.register(email, password)
    }

    init {
        authAppRepository = RepositoryModel(application)
        userLiveData = authAppRepository.getUserLiveData()
    }
}