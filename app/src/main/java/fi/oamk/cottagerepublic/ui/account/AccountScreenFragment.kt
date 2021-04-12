package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountScreenBinding


class AccountScreenFragment : Fragment() {

    private lateinit var binding: FragmentAccountScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_screen,
            container,
            false
        )

        // Open My Cottages
        binding.cottagesLayout.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountCottageScreenFragment)
        }
        // Open My Reservations
        binding.reservationLayout.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_accountReservationsScreenFragment)
        }

        return binding.root
    }

}