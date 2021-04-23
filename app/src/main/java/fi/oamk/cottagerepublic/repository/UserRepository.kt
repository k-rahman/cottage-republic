package fi.oamk.cottagerepublic.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserRepository() {
    private val databaseReference: DatabaseReference = Firebase.database.reference
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userid = firebaseAuth.currentUser


    fun createUser(email: String) {
        databaseReference.child("users").child(userid!!.uid).child("email").setValue(email)
    }

    fun pushCottageToUser( userKey : String, cottageKey : String)
    {
        val childUpdates = hashMapOf<String, Any>(
            "users/${userKey}/cottages/$cottageKey" to true
        )


        databaseReference.updateChildren(childUpdates)
    }

    fun getCurrentUserId(): String {
        if (userid != null)
            return userid.uid

        return "User isn't logged in"
    }
}