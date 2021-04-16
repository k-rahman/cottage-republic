package fi.oamk.cottagerepublic.ui.bookingDetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentBookingDetailScreenBinding

class BookingDetailFragment : Fragment() {
    private lateinit var binding: FragmentBookingDetailScreenBinding
    private lateinit var viewModel: BookingDetailViewModel
    private lateinit var viewModelFactory: BookingDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail_screen, container, false)

        val selectedCottage = BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage
        val selectedDates =
            BookingDetailFragmentArgs.fromBundle(requireArguments()).selectedDates.toList()

        viewModelFactory = BookingDetailViewModelFactory(Application(), selectedCottage, selectedDates)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BookingDetailViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.bookingViewModel = viewModel

        setObservers()

        if (requireActivity().findViewById<View>(R.id.bottom_nav_view).isVisible)
        // hide bottom navigation
            requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        return binding.root
    }

    private fun setObservers() {
        viewModel.navigateToSearch.observe(viewLifecycleOwner, {
            if (it) {
                findNavController()
                    .navigate(BookingDetailFragmentDirections.actionBookingDetailFragmentToSearchFragment())
                viewModel.onSearchNavigated()

                // show bottom navigation
                requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
            }
        })

        viewModel.navigateToSuccess.observe(viewLifecycleOwner, {
        })
    }
}