package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountScreenBinding
import fi.oamk.cottagerepublic.ui.auth.LoginScreenFragment
import fi.oamk.cottagerepublic.ui.auth.AuthViewModel
import fi.oamk.cottagerepublic.util.Resource


class AccountScreenFragment : Fragment() {

    private lateinit var binding: FragmentAccountScreenBinding
    private val authViewModel: AuthViewModel by activityViewModels()

    // The user data in UserViewModel is exposed via LiveData,
    // so to decide where to navigate, you should observe this data.
    // When navigating to AccountScreenFragment, the app shows the AccountScreen if the
    // user data is present. If the user data is null, you are navigated to LoginFragment,
    // user needs to authenticate before seeing their account.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController()

        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginScreenFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_screen,
            container,
            false
        )

        authViewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                // If the user data is null when user reaches the AccountScreenFragment,
                authViewModel.resetLogin()
                // they are redirected to the LoginFragment.
                findNavController().navigate(R.id.loginScreenFragment)
            }
        }
        // Open User Settings
        binding.userSettingsLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.acccountUserSettingsScreenFragment)
        }
        // Open My Cottages
        binding.cottagesLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountCottageScreenFragment)
        }
        // Open My Reservations
//        binding.reservationLayout.setOnClickListener { view: View ->
//            view.findNavController().navigate(R.id.reservation_layout)
//        }
        // Logout of system
        binding.logoutLayout.setOnClickListener {
            // Sign user out of firebase here
            authViewModel.onLogoutClick()
        }

        // observe navigate if true will return to homescreen when user logs out of system
        authViewModel.navigate.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.homeScreenFragment)
                authViewModel.onNavigated()
            }

        }
        return binding.root
    }

}