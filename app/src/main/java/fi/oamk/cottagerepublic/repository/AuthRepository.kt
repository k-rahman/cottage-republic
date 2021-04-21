package fi.oamk.cottagerepublic.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await


class AuthRepository() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()


    companion object {
        const val TAG = "AuthRepository"
    }

    init {

        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.value = false
            Log.v("init repo", "user logged in")
        } else {
            loggedOutLiveData.value = true
            Log.v("init repo", "user logged out")
        }
        Log.v("init repo", "init success")
    }


    fun register(username: String?, password: String?) {
        Log.v("Test1", "Registering..")
        Log.v("Password and username = ", "$username $password")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(
                ) { task ->
                    if (task.isSuccessful) {
                        userLiveData.postValue(firebaseAuth.currentUser)
                        Log.v("Test2", "register success")

                    } else {
                        userLiveData.postValue(null)
                        Log.v("Test2", "register fail")

                    }
                }
        }


    }


    suspend fun login(username: String?, password: String?): Resource<FirebaseUser> {
        Log.v("test1", "Login in..")
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        val authResult = firebaseAuth.signInWithEmailAndPassword(username, password).await()
        return Resource.Success(authResult.user)
//                .addOnCompleteListener(
//                ) { task ->
//                    if (task.isSuccessful) {
//                        userLiveData.postValue(firebaseAuth.currentUser)
//                        Log.v("test2", "login Success")
//                        Log.i(TAG, "Successfully signed in user ${firebaseAuth.currentUser?.email}")
//
//                    } else {
//                        userLiveData.postValue(null)
//
//                        Log.v("test2", "Login fail")
//                        Log.i(TAG, "Login failure")
//                    }

//                }


//    }
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

