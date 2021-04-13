package fi.oamk.cottagerepublic.ui.auth

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import fi.oamk.cottagerepublic.R


class RegisterFragmentEmail : Fragment(),View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.navigate_button_email).setOnClickListener(this)
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    override fun onClick(v: View?) {
        val email = view?.findViewById<EditText>(R.id.userEmail)?.text.toString()
            if (email.isEmailValid()) {
                val bundle = bundleOf("email" to email)
                navController.navigate(
                    R.id.action_registerFragmentEmail_to_registerFragmentPassword,
                    bundle
                )
            }
            else {
                view?.findViewById<TextInputLayout>(R.id.registerEmailLayout)?.error = "Please type in correct email"
            }
        }

}


