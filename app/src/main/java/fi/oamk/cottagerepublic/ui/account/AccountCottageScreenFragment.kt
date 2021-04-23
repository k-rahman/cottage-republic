package fi.oamk.cottagerepublic.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentAccountCottageScreenBinding
import fi.oamk.cottagerepublic.util.Resource


class AccountCottageScreenFragment : Fragment() {
    private lateinit var binding: FragmentAccountCottageScreenBinding
    private lateinit var viewModel: MyCottagesViewModel
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


        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner

        setMyCottagesAdapters()
        setObservers()

        // Open form to upload new cottage
        binding.fabAddCottage.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_accountCottageScreenFragment_to_CreateCottageFragment)
        }

        return binding.root
    }

    private fun setMyCottagesAdapters() {
        myCottagesAdapter = MyCottagesAdapter(MyCottageListener {
            // handle my cottage click
//            Toast.makeText(context, "${it.cottageLabel}", Toast.LENGTH_SHORT).show()
            viewModel.onMyCottageClicked(it)
//            Toast.makeText(context, "clicked here myCottageAdapter", Toast.LENGTH_LONG).show()
        }, DeleteCottageListener { cottageId ->
            Toast.makeText(context, cottageId, Toast.LENGTH_SHORT).show()
            // want to invoke the dialog here
            confirmDelete()
        })



        with(binding) {
            ownersCottageList.adapter = myCottagesAdapter
        }
    }

    private fun setObservers() {

        viewModel.myCottagesList.observe(viewLifecycleOwner) {
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
                        "An error has occurred:${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // navigate to the cottage edit form and populate all fields with cottage details
        viewModel.navigateToMyCottage.observe(viewLifecycleOwner, Observer { cottage ->
            // Clicked cottage details are being passed to screen for edit
            cottage?.let {
                navigateToMyCottage(it)
            }

        })

    }


    private fun confirmDelete() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(R.string.title))
                .setMessage(resources.getString(R.string.supporting_text))
                .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                    // Respond to negative button press
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    // Respond to positive button press
                    // Need to delete the cottage from the database
                    Toast.makeText(
                    context,
                    "Deleted Cottage",
                    Toast.LENGTH_SHORT
                ).show()
                }
                .show()
        }
    }

    private fun navigateToMyCottage(cottage: Cottage) {
        // pass the cottage as an argument to the edit form screen
        // receive the argument inside the AddCottageFormFragment
        findNavController().navigate(
            AccountCottageScreenFragmentDirections.actionAccountCottageScreenFragmentToAddCottageFormFragment(cottage)
        )
        viewModel.onCottageNavigated()
    }





}