package fi.oamk.cottagerepublic.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RepositoryModel(private val application: Application) {
    private val firebaseAuth: FirebaseAuth
    val userLiveData: MutableLiveData<FirebaseUser>
    val loggedOutLiveData: MutableLiveData<Boolean>

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }


    fun register(email: String?, password: String?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.mainExecutor,
                    { task ->
                        if (task.isSuccessful) {
                            userLiveData.postValue(firebaseAuth.currentUser)
                        } else {
                            Toast.makeText(
                                application.applicationContext,
                                "Registration Failure: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }


    }

    fun login(email: String?, password: String?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(application.mainExecutor,
                    { task ->
                        if (task.isSuccessful) {
                            userLiveData.postValue(firebaseAuth.currentUser)
                        } else {
                            Toast.makeText(
                                application.applicationContext,
                                "Login Failure: " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }




}

