package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
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


        fun getCurrentUserId(): String {
            if (userid != null)
                return userid?.uid.toString()
            return "User isn't logged in"
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
                    Log.i("failure" , "there is some problem getting the data")
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
                    databaseReference.child("users").child(userid!!.uid).child("fname").setValue(fname)
                }
                if (lname != userProfile.child("lname").value.toString()) {
                    databaseReference.child("users").child(userid!!.uid).child("lname").setValue(lname)
                }
                if (phone != userProfile.child("phone").value.toString()) {
                    databaseReference.child("users").child(userid!!.uid).child("phone").setValue(phone)
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
    fun getUserReservations(): Task<DataSnapshot> {
        val testid = "6EAP6t8B8wROG6OYsPwzaHBntjE2"
        val data = databaseReference.child("users").child(testid).child("reservations").get()
        return data
    }

}

