package fi.oamk.cottagerepublic.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentRegisterScreenBinding
import fi.oamk.cottagerepublic.util.Resource


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterScreenBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_screen, container, false)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.email.observe(viewLifecycleOwner) {
            viewModel.showEmailErrors()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.showPasswordErrors()
        }

        viewModel.confirmPassword.observe(viewLifecycleOwner) {
            viewModel.showConfirmPasswordErrors()
        }

        viewModel.isRegistered.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(), "Registered successfully", Toast.LENGTH_SHORT).show()
                    viewModel.addUserEmailToDb(it.data.toString())
                    findNavController().navigate(R.id.acccountUserSettingsScreenFragment, null, getNavOptions())
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(android.R.anim.slide_in_left)
            .setPopExitAnim(android.R.anim.slide_out_right)
            .build()
    }
}


