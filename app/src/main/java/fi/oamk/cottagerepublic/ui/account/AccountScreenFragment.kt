package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountScreenBinding
import fi.oamk.cottagerepublic.ui.auth.LoginScreenFragment
import fi.oamk.cottagerepublic.ui.auth.LoginScreenViewModel


class AccountScreenFragment : Fragment() {
    private lateinit var binding: FragmentAccountScreenBinding


    // The user data in UserViewModel is exposed via LiveData,
    // so to decide where to navigate, you should observe this data.
    // When navigating to AccountScreenFragment, the app shows the AccountScreen if the
    // user data is present. If the user data is null, you are navigated to LoginFragment,
    // user needs to authenticate before seeing their account.
    // **************
    private val loginScreenViewModel: LoginScreenViewModel by activityViewModels()

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

    // **************

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
        // Open User Settings
        binding.userSettingsLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountSettingScreenFragment)
        }
        // Open My Cottages
        binding.cottagesLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountCottageScreenFragment)
        }
        // Open My Reservations
        binding.reservationLayout.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountReservationsScreenFragment)
        }
        // Logout of system
        binding.logoutLayout.setOnClickListener { view: View ->
            loginScreenViewModel.onLogoutClick()
        }

        loginScreenViewModel.navigateToHome.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.homeScreenFragment)
                loginScreenViewModel.onHomeNavigated()
            }

        }


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        loginScreenViewModel.userIsLoggedIn.observe(viewLifecycleOwner, Observer { user ->
            if (user == true)
                navController.navigate(R.id.loginScreenFragment)
        })
    }

}