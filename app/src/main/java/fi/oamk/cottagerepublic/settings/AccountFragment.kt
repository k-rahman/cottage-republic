package fi.oamk.cottagerepublic.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAccountScreenBinding


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentAccountScreenBinding>(inflater, R.layout.fragment_account_screen, container, false)
        binding.cottagesLayout.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountScreenFragment_to_cottagesScreenFragment)
        }
        return binding.root
    }

}