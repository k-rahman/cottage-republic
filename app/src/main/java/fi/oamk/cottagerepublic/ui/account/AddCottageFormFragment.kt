package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentAddCottageFormBinding


class AddCottageFormFragment : Fragment() {

    private lateinit var binding: FragmentAddCottageFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_cottage_form,
            container,
            false
        )
        val args = AddCottageFormFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "Cottage: ${args.cottage.cottageLabel}", Toast.LENGTH_SHORT).show()


//        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


}