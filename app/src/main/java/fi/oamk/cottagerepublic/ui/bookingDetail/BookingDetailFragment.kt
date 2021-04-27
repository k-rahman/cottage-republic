package fi.oamk.cottagerepublic.ui.bookingDetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentBookingDetailScreenBinding
import fi.oamk.cottagerepublic.ui.auth.AuthViewModel

class BookingDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookingDetailScreenBinding
    private lateinit var viewModel: BookingDetailViewModel
    private lateinit var viewModelFactory: BookingDetailViewModelFactory
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail_screen, container, false)

        initToolbar()
        initViewModel()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.bookingViewModel = viewModel

        // hide navbar
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        setObservers()

        return binding.root
    }

    private fun initToolbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)
    }

    private fun initViewModel() {
        val selectedCottage = BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage
        val selectedDates =
            BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedDates.toList()

        viewModelFactory = BookingDetailViewModelFactory(Application(), selectedCottage, selectedDates)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookingDetailViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.navigateToSuccess.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    BookingDetailFragmentDirections.actionBookingDetailFragmentToPaymentFragment(
                        viewModel.selectedCottage.price
                    )
                )
                viewModel.onSucessNavigated()
            }
        })

        authViewModel.user.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                binding.confirmButton.isEnabled = true
            } else {
                binding.confirmButton.isEnabled = false
                binding.signupButtonWrapper.visibility = View.VISIBLE
            }
        })

        viewModel.navigateToLogin.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.loginScreenFragment, null, getNavOptions())
                viewModel.onLoginNavigated()
            }
        })

        viewModel.navigateToRegister.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(R.id.registerFragment, null, getNavOptions())
                viewModel.onRegisterNavigated()
            }
        })
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