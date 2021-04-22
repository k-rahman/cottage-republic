package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
        Log.v("init repo", "init success")
    }

    fun login(username: String, password: String) {
        Log.v("test1","Login in..")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userLiveData.postValue(firebaseAuth.currentUser)
                        Log.v("test2", "login Success")
                    } else {
                        Log.v("test2", "Login fail")
                    }
                }
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
        //Log.v("test2", "Logged out")
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }


    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }
    fun setUserLiveData(username: FirebaseUser?) {
        userLiveData.postValue(username)
    }


}

