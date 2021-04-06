package fi.oamk.cottagerepublic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation


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

        view.findViewById<Button>(R.id.navigate_button_end).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_registerFragmentEnd_to_mainScreen, null))
    }

}