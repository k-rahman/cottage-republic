package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await


class UserRepository(private val databaseReference: DatabaseReference) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(databaseReference: DatabaseReference): UserRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = UserRepository(databaseReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private val auth = FirebaseAuth.getInstance()
    private lateinit var userProfile: DataSnapshot
    private var userid = auth.currentUser


    fun saveEmailOnRegister(email: String) {
        if (auth.currentUser != null)
            databaseReference.child(getCurrentUserId()).setValue(email)
    }

    fun getCurrentUserData(
        email: MutableLiveData<String>,
        fname: MutableLiveData<String>,
        lname: MutableLiveData<String>,
        phone: MutableLiveData<String>
    ): Boolean {
        if (auth.currentUser != null) {
            databaseReference.child(getCurrentUserId()).get().addOnSuccessListener {
                if (it.child("email").value != null) email.postValue(it.child("email").value.toString())
                if (it.child("phone").value != null) phone.postValue(it.child("phone").value.toString())
                if (it.child("fname").value != null) fname.postValue(it.child("fname").value.toString())
                if (it.child("lname").value != null) lname.postValue(it.child("lname").value.toString())
                userProfile = it
                Log.i("data check", userProfile.toString())
            }.addOnFailureListener {
                Log.i("failure", "there is some problem getting the data")
            }
            return true
        } else {
            return false
        }
    }

    fun updateUserData(fname: String, lname: String, phone: String): Boolean {
        //if not dont have logged in user will return false
        if (userid != null) {
            if (fname != userProfile.child("fname").value.toString()) {
                databaseReference.child(userid!!.uid).child("fname").setValue(fname)
            }
            if (lname != userProfile.child("lname").value.toString()) {
                databaseReference.child(userid!!.uid).child("lname").setValue(lname)
            }
            if (phone != userProfile.child("phone").value.toString()) {
                databaseReference.child(userid!!.uid).child("phone").setValue(phone)
            }
            Log.i("data update", "data saved")
            // if there is a logged in user to save data
            return true
        } else {
            Log.i("data update", "data save failed")
            return false
        }
    }

    fun getUserData(): DataSnapshot {
        return userProfile
    }

    suspend fun deleteCottageIdByHostId(cottageId: String) {
        databaseReference
            .child(getCurrentUserId())
            .child("cottages")
            .child(cottageId)
            .removeValue().await()
    }

    suspend fun getHostDataById(hostId: String): Resource<Any> {
        return try {
            val snapshot = databaseReference.child(hostId).get().await()
            val userData = initUser(snapshot)
            Resource.Success(userData)
        } catch (e: Exception) {
            Resource.Failure(e.message!!)
        }
    }

    suspend fun getCottagesKeysByHostId(hostId: String): List<String> {
        val cottageKeys = mutableListOf<String>()

        val snapshot = databaseReference.child(hostId).child("cottages").get().await()
        for (cottageKey in snapshot.children) {
            cottageKeys.add(cottageKey.key.toString())
        }
        return cottageKeys

    }

    private fun initUser(snapshot: DataSnapshot): User {
        val values = snapshot.value as HashMap<*, *>
        val user = User()
        with(user) {
            userId = snapshot.key.toString() // this need to be when creating the user

            if (values["email"] != null)
                email = values["email"].toString()

            if (values["fname"] != null)
                firstName = values["fname"].toString()

            if (values["lname"] != null)
                lastName = values["lname"].toString()

            if (values["phone"] != null)
                phone = values["phone"].toString()

            if (values["cottages"] != null)
                for (cottageId in values["cottages"] as HashMap<*, *>)
                    cottages.add(cottageId.key.toString())

            if (values["reservations"] != null)
                for (reservationId in values["reservations"] as HashMap<*, *>)
                    reservations.add(reservationId.key.toString())
        }
        return user
    }

    fun pushCottageToUser(userKey: String, cottageKey: String) {
        val childUpdates = hashMapOf<String, Any>(
            "${userKey}/cottages/$cottageKey" to true
        )

        databaseReference.updateChildren(childUpdates)
    }

    fun getCurrentUserId(): String {
        return getCurrentUser().value!!.uid
    }

    // Using MutableLiveData to notify AccountScreenFragment when current user has changed
    fun getCurrentUser(): MutableLiveData<FirebaseUser> {
        return MutableLiveData(firebaseAuth.currentUser)
    }
}

