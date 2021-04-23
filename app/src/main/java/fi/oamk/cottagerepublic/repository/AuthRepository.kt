package fi.oamk.cottagerepublic.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await


class  AuthRepository(private val firebaseAuth: FirebaseAuth) {
//    private val currentUser = MutableLiveData<Resource<Any>>()
//    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()


    /**
     * Singleton copied over from CottageRepository
     * Should only create 1 instance of the repository, as repositories are not going to change
     * Singleton object here helps to create only one instance of the object.
     * A user is logged in here now so, multiple logins can't be possible
     */
    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null
        fun getInstance(firebaseAuth: FirebaseAuth): AuthRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = AuthRepository(firebaseAuth)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    // AuthViewModel will invoke this suspend function and run in coroutine
    suspend fun login(username: String, password: String): Resource<Any> {
        // make a network call to firebase
        return try {
            // If successful return the user
            val fetchUser = firebaseAuth.signInWithEmailAndPassword(username, password).await()
            Resource.Success(fetchUser.user!!)
        } catch (e: Exception) {
            // If no user is fetched return failure
            Resource.Failure(e.message!!)
        }
    }

    // Log current user out from firebase
    fun logOut() {
        firebaseAuth.signOut()
    }



//    fun register(username: String?, password: String?) {
//        Log.v("Test1", "Registering..")
//        Log.v("Password and username = ", "$username $password")
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            firebaseAuth.createUserWithEmailAndPassword(username, password)
//                .addOnCompleteListener(
//                ) { task ->
//                    if (task.isSuccessful) {
//                        userLiveData.postValue(firebaseAuth.currentUser)
//                        Log.v("Test2", "register success")
//
//                    } else {
//                        userLiveData.postValue(null)
//                        Log.v("Test2", "register fail")
//
//                    }
//                }
//        }
//    }

}

