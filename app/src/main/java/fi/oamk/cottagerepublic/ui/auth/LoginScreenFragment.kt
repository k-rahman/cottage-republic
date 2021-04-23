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
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentLoginScreenBinding
import fi.oamk.cottagerepublic.util.Resource

class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var savedStateHandle: SavedStateHandle

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)


        authViewModel.isLoggedIn.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    // Could add loading screen here
                }
                is Resource.Success -> {
                    // Setting this state using SavedStateHandle ensures that the state persists through process death.
                    savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    // pop loginScreen off the stack
                    findNavController().popBackStack()
                    // reset values after logging in to system
                    authViewModel.resetLogin()
                }
                is Resource.Failure -> {
                    // Show any errors if input fields are incorrect
                    authViewModel.showFailureErrorMessage(it.message)
                }
            }
        }

        authViewModel.navigateToRegister.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    LoginScreenFragmentDirections.actionLoginScreenFragmentToRegisterFragmentEmail()
                )
                authViewModel.onRegisterNavigated()
            }
        }

        binding.loginViewModel = authViewModel

        binding.lifecycleOwner = this

        return binding.root
    }
}