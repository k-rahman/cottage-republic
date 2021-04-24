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
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.repository.UserRepository


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
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                    val userId = UserRepository(database).getCurrentUserId()
                    database.child(userId).child("email").setValue(email)
                    Log.v("Test2", "register success")
                    navController.navigate(R.id.accountScreenFragment, null, getNavOptions())

            } else {
                Log.v("Test2", "register fail")
                navController.navigate(R.id.loginScreenFragment, null, getNavOptions())
            }
        }
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(android.R.anim.slide_in_left)
            .setPopExitAnim(android.R.anim.slide_out_right)
            .setPopUpTo(R.id.loginScreenFragment, false)
            .build()
    }
}