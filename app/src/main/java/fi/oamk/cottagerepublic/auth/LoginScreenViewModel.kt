package fi.oamk.cottagerepublic.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fi.oamk.cottagerepublic.repository.RepositoryModel

class LoginScreenViewModel : ViewModel() {
    lateinit var repository: RepositoryModel
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private var _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate
//    val username: LiveData<String>
//        get() = _username

    fun onRegisterClick()
    {

    }

//    fun onLoginClick(username: String , password: String)
fun onLoginClick()
    {
        //Call login function from repository
        //if response successful


        _navigate.value = true


    }

    fun onLoginClick(email: String?, password: String?) {
        repository.login(email, password)
    }

    fun doneNavigate()
    {
        _navigate.value = false
    }

}