package fi.oamk.cottagerepublic.repository

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class AuthRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser>
    private val loggedOutLiveData: MutableLiveData<Boolean>


    init {

        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
        Log.v("init repo", "init success")
    }


    fun register(username: String?, password: String?) {
        Log.v("Test1", "Registering..")
        Log.v("Password and username = ", username + " " + password)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(application.mainExecutor,
                        { task ->
                            if (task.isSuccessful) {
                                userLiveData.postValue(firebaseAuth.currentUser)
                                Log.v("Test2", "register succes")
                                Toast.makeText(
                                        application.applicationContext,
                                        "Registration Succes :) ",
                                        Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.v("Test2", "register fail")
                                Toast.makeText(
                                        application.applicationContext,
                                        "Registration Failure: " + task.exception!!.message,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
        }


    }

    fun login(username: String?, password: String?) {
        Log.v("test1","Login in..")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(application.mainExecutor,
                        { task ->
                            if (task.isSuccessful) {
                                userLiveData.postValue(firebaseAuth.currentUser)
                                Log.v("test2", "login Success")
                            } else {
                                Toast.makeText(
                                        application.applicationContext,
                                        "Login Failure: " + task.exception!!.message,
                                        Toast.LENGTH_SHORT
                                ).show()
                                Log.v("test2", "Login fail")
                            }
                        })
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
        Log.v("test2", "Logged out")
    }

    fun fillInBoxes (){
        Toast.makeText(
            application.applicationContext,
            "please fill in all boxes",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    fun getLoggedOutLiveData(): MutableLiveData<Boolean> {
        return loggedOutLiveData
    }



}

