package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.reflect.KFunction1


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


     fun register(username: String, password: String, kFunction0: KFunction1<Boolean, Unit>) {
        firebaseAuth.signOut()
        Log.v("test", "Registering..")
        Log.v("Password and username = ", "$username $password")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userLiveData.postValue(firebaseAuth.currentUser)
                        Log.v("Test2", "register success")
                        kFunction0(true)
                    } else {
                        Log.v("Test2", "register fail")
                        kFunction0(false)
                    }
                }

        }
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

}

