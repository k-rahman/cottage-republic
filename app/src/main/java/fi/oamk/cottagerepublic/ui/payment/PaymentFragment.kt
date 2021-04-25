package fi.oamk.cottagerepublic.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentPaymentScreenBinding

class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentPaymentScreenBinding
    private lateinit var viewModelFactory: PaymentViewModelFactory
    private lateinit var viewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_screen, container, false)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        initViewModel()
        setObservers()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    private fun initViewModel() {
        val total = PaymentFragmentArgs.fromBundle(requireArguments()).total

        viewModelFactory = PaymentViewModelFactory(total)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PaymentViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.navigateToHome.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().popBackStack()
                viewModel.onHomeNavigated()
            }
        })
    }
}
