package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.User
import fi.oamk.cottagerepublic.databinding.FragmentAccountSettingsScreenBinding
import fi.oamk.cottagerepublic.util.Resource


class AccountUserSettingsScreenFragment : Fragment() {
    private lateinit var binding: FragmentAccountSettingsScreenBinding
    private lateinit var viewModel: AccountUserSettingsScreenViewModel
    private lateinit var disableBackClick: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_settings_screen, container, false)

        viewModel = ViewModelProvider(this).get(AccountUserSettingsScreenViewModel::class.java)

        // hide navbar
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        setObservers()
        disableBackButton()
        setConditionalNavigation()


        binding.userSettingsViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun setObservers() {
        viewModel.userData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    viewModel.setUserData(it.data as User)
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.saveStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    createSnackbar("User information was saved successfully")
                }
                is Resource.Failure -> {
                    createSnackbar("Failed to save user information")
                }
            }
        }

        viewModel.navigateToLogin.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack(R.id.registerFragment, true)

                val loginDestination = findNavController().graph.findNode(R.id.loginScreenFragment)
                if (findNavController().currentDestination != loginDestination)
                    findNavController().navigate(R.id.loginScreenFragment, null, getNavOptions())

                disableBackClick.isEnabled = false
                viewModel.onLoginNavigated()
            }
        }

        viewModel.navigateToProfile.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().popBackStack(R.id.accountScreenFragment, false)
                viewModel.onProfileNavigated()
            }
        }
    }

    private fun setConditionalNavigation() {
        val registerDestination = findNavController().graph.findNode(R.id.registerFragment)
        if (findNavController().previousBackStackEntry?.destination == registerDestination) {
            viewModel.loginFragment = true
            disableBackClick.isEnabled = true
        }
    }

    private fun disableBackButton() {
        disableBackClick = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, disableBackClick)
    }

    private fun createSnackbar(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(android.R.anim.slide_in_left)
            .setPopExitAnim(android.R.anim.slide_out_right)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
    }
}