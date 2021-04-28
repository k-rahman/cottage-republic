package fi.oamk.cottagerepublic.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await


class AuthRepository(private val firebaseAuth: FirebaseAuth) {

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

    suspend fun register(email: String, password: String): Resource<Any> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(email)
        } catch (e: Exception) {
            Resource.Failure(e.message.toString())
        }
    }

    // Log current user out from firebase
    fun logOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUserId(): String? {
        if(getCurrentUser().value != null) {
            return getCurrentUser().value!!.uid
        }

        return null
    }

    // Using MutableLiveData to notify AccountScreenFragment when current user has changed
    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        return MutableLiveData(firebaseAuth.currentUser)
    }
}

