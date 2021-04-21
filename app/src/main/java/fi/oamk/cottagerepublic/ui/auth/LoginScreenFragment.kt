package fi.oamk.cottagerepublic.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentLoginScreenBinding
import fi.oamk.cottagerepublic.util.Resource

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    private val loginScreenViewModel: LoginScreenViewModel by activityViewModels()
    private lateinit var savedStateHandle: SavedStateHandle


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)

        loginScreenViewModel.navigateToRegister.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    LoginScreenFragmentDirections.actionLoginScreenFragmentToRegisterFragmentEmail()
                )
                loginScreenViewModel.onRegisterNavigated()
            }
        }

        binding.loginViewModel = loginScreenViewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)

        val usernameET = binding.usernameLoginInput
        val passwordET = binding.passwordLoginInput
        val loginBtn = binding.loginButton

        loginBtn.setOnClickListener {
            val username = usernameET.text.toString()
            val password = passwordET.text.toString()
            login(username, password)
        }

    }

    fun login(username: String, password: String) {
        loginScreenViewModel.login(username, password).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    findNavController().popBackStack()
                }
                is Resource.Failure -> {
                    Toast.makeText(context, "Login not successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}