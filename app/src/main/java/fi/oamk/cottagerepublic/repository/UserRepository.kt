package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class UserRepository {
    private val databaseReference: DatabaseReference = Firebase.database.reference
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userProfile: DataSnapshot
    private var userid = firebaseAuth.currentUser

    fun createUser(email: String) {
        databaseReference.child("users").child(userid!!.uid).child("email").setValue(email).addOnSuccessListener {
            return@addOnSuccessListener
        }.addOnFailureListener { return@addOnFailureListener }
    }

    fun getCurrentUserId() : String {
        if (userid != null)
            return userid?.uid.toString()

        throw Exception("User isn't logged in")
    }
    fun getCurrentUserData(email: MutableLiveData<String>, fname: MutableLiveData<String>, lname: MutableLiveData<String>, phone: MutableLiveData<String>): Boolean {

        if (firebaseAuth.currentUser != null) {
            databaseReference.child("users").child(userid!!.uid).get().addOnSuccessListener {

                if (it.child("email").value != null) email.postValue(it.child("email").value.toString())
                if (it.child("phone").value != null) phone.postValue(it.child("phone").value.toString())
                if (it.child("fname").value != null) fname.postValue(it.child("fname").value.toString())
                if (it.child("lname").value != null) lname.postValue(it.child("lname").value.toString())

                userProfile = it
                Log.i("data check", userProfile.toString())
            }.addOnFailureListener {
                throw Exception("Failure loading data from server")
            }
            return true
        }
        else{
            return false
        }

    }
    fun updateUserData(fname: String, lname: String, phone: String): Boolean {
        if (userid !=null) {
           if(fname != userProfile.child("fname").value.toString()){
               databaseReference.child("users").child(userid!!.uid).child("fname").setValue(fname).addOnFailureListener { return@addOnFailureListener }
           }
            if(lname != userProfile.child("lname").value.toString()){
                databaseReference.child("users").child(userid!!.uid).child("lname").setValue(lname).addOnFailureListener { return@addOnFailureListener }
            }
            if(phone != userProfile.child("phone").value.toString()){
                databaseReference.child("users").child(userid!!.uid).child("phone").setValue(phone).addOnFailureListener { return@addOnFailureListener }
            }
            Log.i("data check", "data saved")
            return true
        }
        else{
            return false
        }
    }
    fun getUserData(): DataSnapshot {
        return userProfile
    }

}
