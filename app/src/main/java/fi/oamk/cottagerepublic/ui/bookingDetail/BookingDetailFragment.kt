package fi.oamk.cottagerepublic.ui.bookingDetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentBookingDetailScreenBinding
import fi.oamk.cottagerepublic.ui.auth.AuthViewModel
import fi.oamk.cottagerepublic.ui.auth.LoginScreenFragment

class BookingDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookingDetailScreenBinding
    private lateinit var viewModel: BookingDetailViewModel
    private lateinit var viewModelFactory: BookingDetailViewModelFactory
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle

        savedStateHandle.getLiveData<Boolean>(LoginScreenFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, { success ->
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail_screen, container, false)

        initViewModel()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.bookingViewModel = viewModel

        // hide navbar
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        setObservers()

        return binding.root
    }

    private fun initViewModel() {
        val selectedCottage = BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage
        val selectedDates =
            BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedDates.toList()

        viewModelFactory = BookingDetailViewModelFactory(Application(), selectedCottage, selectedDates)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookingDetailViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.navigateToCottageDetail.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigateUp()
                viewModel.onCottageDetailNavigated()
            }
        })

        viewModel.navigateToSuccess.observe(viewLifecycleOwner, {
        })

        authViewModel.user.observe(viewLifecycleOwner, { user ->
            if (user == null) {
//                findNavController().navigate(R.id.loginScreenFragment)
                requireActivity().let {
                    AlertDialog.Builder(it).setMessage("Please log in first").setTitle("Login").create().show()
                }

            }
        })
    }
}