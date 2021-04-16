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
        databaseReference.child("users").child(userid!!.uid).child("email").setValue(email).addOnSuccessListener {
            return@addOnSuccessListener
        }.addOnFailureListener { return@addOnFailureListener }
    }

    fun getCurrentUserId() : String {
        if (userid != null)
            return userid.uid

        throw Exception("User isn't logged in")
    }
}