package fi.oamk.cottagerepublic.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.repository.RepositoryModel


class LoginScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RepositoryModel
    val userLiveData: MutableLiveData<FirebaseUser>
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate



    fun onLoginClick(username: String?, password: String?) {
        repository?.login(username, password)

        _navigate.value = true

    }

    fun onRegisterClick(username: String?, password: String?) {
        repository?.register(username, password)
        Log.v("Test", "Registration has been clicked")
    }



    fun doneNavigate()
    {
        _navigate.value = false
    }

    init {
        repository = RepositoryModel(application)
        userLiveData = repository.getUserLiveData()
    }

}