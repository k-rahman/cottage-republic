package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountCottageScreenBinding
import fi.oamk.cottagerepublic.ui.home.CottageListener


class AccountCottageScreenFragment : Fragment() {

    private lateinit var binding: FragmentAccountCottageScreenBinding
    private lateinit var myCottagesAdapter: MyCottagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_cottage_screen,
            container,
            false
        )

        // Open form to upload new cottage
        binding.fabAddCottage.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountCottageScreenFragment_to_addCottageFormFragment)
        }
        return binding.root
    }

    private fun setMyCottagesAdapters() {
//        myCottagesAdapter = MyCottagesAdapter(CottageListener { cottageName ->
//
//        })
        with(binding) {

        }
    }
}