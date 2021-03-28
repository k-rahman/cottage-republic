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
        val email = view?.findViewById<TextInputEditText>(R.id.userEmail).toString()
        when(v!!.id){
            R.id.navigate_button_required ->{
                if (!TextUtils.isEmpty(email)){
                    val bundle = bundleOf("email" to email)
                    navController.navigate(R.id.action_registerFragmentPassword_to_registerFragmentEnd,bundle)
                }
            }
        }
    }
}