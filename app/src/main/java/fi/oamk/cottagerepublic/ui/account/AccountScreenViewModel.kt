package fi.oamk.cottagerepublic.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.repository.UserRepository

class AccountScreenViewModel() : ViewModel() {


    private val userDataSource = UserRepository.getInstance(Firebase.database.getReference("users"))
    private val authDataSource = AuthRepository.getInstance(FirebaseAuth.getInstance())

    val email = MutableLiveData("")
    val fname = MutableLiveData("")
    val lname = MutableLiveData("")
    val phone = MutableLiveData("")


    fun initUser(){
        // will return true if get data and false if something goes wrong
        val userId = authDataSource.getCurrentUserId()
        userDataSource.getCurrentUserData(userId, email, fname, lname, phone)
    }

}