package fi.oamk.cottagerepublic.ui.auth


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.repository.AuthRepository


class RegisterFragmentEnd : Fragment() {

    lateinit var email: String
    lateinit var password: String
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = requireArguments().getString("email").toString().trim()
        password = requireArguments().getString("password").toString().trim()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_end, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val message = "New account for $email"
        view.findViewById<TextView>(R.id.newUserConfirmEmail).text = message
        val message2 = "The password for $password"
        view.findViewById<TextView>(R.id.newUserConfirmPassword).text = message2

        view.findViewById<Button>(R.id.navigate_button_end).setOnClickListener {
            register()
        }
    }

    private fun register() {
        Log.v("test", "Registering..")
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val database = Firebase.database.getReference("users")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (firebaseAuth.currentUser != null) {
                        AuthRepository().setUserLiveData(firebaseAuth.currentUser)
                        database.child(firebaseAuth.currentUser!!.uid).child("email").setValue(email)
                        Log.v("Test2", "register success")
                        navController.navigate(R.id.accountScreenFragment)
                    }
                } else {
                    Log.v("Test2", "register fail")
                    navController.navigate(R.id.loginScreenFragment)
                }
            }
        }
    }
}