package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
        
        return binding.root
    }

}