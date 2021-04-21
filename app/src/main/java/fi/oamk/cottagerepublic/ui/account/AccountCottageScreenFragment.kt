package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentAccountCottageScreenBinding
import fi.oamk.cottagerepublic.util.Resource


class AccountCottageScreenFragment : Fragment() {

    private lateinit var viewModel: MyCottagesViewModel
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

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(this).get(MyCottagesViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        // This is necessary to use listener bindings
//        binding.myCottageViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner
        


        // Open form to upload new cottage
        binding.fabAddCottage.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountCottageScreenFragment_to_addCottageFormFragment)
        }
        setMyCottagesAdapters()
        setObservers()
        return binding.root
    }

    private fun setMyCottagesAdapters() {
        myCottagesAdapter = MyCottagesAdapter(MyCottageListener {
            Toast.makeText(context, "clicked here myCottageAdapter", Toast.LENGTH_LONG).show()

        })

        with(binding) {
            ownersCottageList.adapter = myCottagesAdapter

        }
    }

    private fun setObservers() {

        viewModel.myCottages.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressIndicator.show()
                }
                is Resource.Success -> {

                    // ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
                    var cottageList = it.data as MutableList<Cottage>
                    myCottagesAdapter.submitList(
                        cottageList.toList().reversed()
                    ) // pass a copy of the list to be diffed

                    binding.progressIndicator.hide()
                }
                is Resource.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "An error has occurred:${it.throwable.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

}