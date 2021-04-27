package fi.oamk.cottagerepublic.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await


class UserRepository(private val databaseReference: DatabaseReference) {

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

    suspend fun getUserData(userId: String): Resource<Any> {
        return try {
            val result = databaseReference.child(userId).get().await()
            val user = initUser(result)
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Failure(e.message!!)
        }
    }

    suspend fun updateUserData(user: User): Resource<Any> {
        val updatedUser = hashMapOf<String, Any>(
            "${user.userId}/fname" to user.firstName,
            "${user.userId}/lname" to user.lastName,
            "${user.userId}/phone" to user.phone
        )

        return try {
            val result = databaseReference.updateChildren(updatedUser).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Failure(e.message!!)
        }
    }

    suspend fun deleteCottageIdByHostId(userId: String, cottageId: String) {
        databaseReference
            .child(userId)
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

    suspend fun deleteCottageIdByUserId(cottageId: String, userId: String) {
        databaseReference
            .child(userId)
            .child("cottages")
            .child(cottageId)
            .removeValue()
            .await()
    }

    suspend fun getCottagesKeysByHostId(hostId: String): MutableList<String> {
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

    fun addUserEmail(userId: String, email: String) {
        databaseReference.child(userId).child("email").setValue(email)
    }
}

