package fi.oamk.cottagerepublic

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText


class RegisterFragmentPassword : Fragment() , View.OnClickListener {
    lateinit var navController: NavController
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = requireArguments().getString("email").toString().trim()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_password, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.navigate_button_required).setOnClickListener(this)
        val message = "New account for $email"
        view.findViewById<TextView>(R.id.newUserEmail).text = message
    }
    override fun onClick(v: View?) {

        val password = view?.findViewById<TextInputEditText>(R.id.userpassword1)?.text.toString()
        val password2 = view?.findViewById<TextInputEditText>(R.id.userpassword2)?.text.toString()

        when(v!!.id){
            R.id.navigate_button_required ->{
                // Check if empty or does password is equal to password2
                if (!TextUtils.isEmpty(password) && (password == password2) ){
                    val bundle = bundleOf("email" to email,"password" to password)
                    navController.navigate(R.id.action_registerFragmentPassword_to_registerFragmentEnd,bundle)
                }
            }
        }
    }
}