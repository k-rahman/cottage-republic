package fi.oamk.cottagerepublic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.*
import fi.oamk.cottagerepublic.databinding.FragmentHomeScreenBinding
import fi.oamk.cottagerepublic.util.HorizontalItemDecoration

class HomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var popularCottagesAdapter: PopularCottagesAdapter
    private lateinit var popularDestinationsAdapter: PopularDestinationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_screen, container, false
        )

        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        // Get a reference to the ViewModel associated with this fragment.
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access
        // to all the data in the ViewModel
        // This is necessary to use listener bindings
        binding.homeViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        setHomeAdapters()
        setObservers()

        return binding.root
    }

    private fun setHomeAdapters() {
        popularDestinationsAdapter = PopularDestinationAdapter(DestinationListener { destinationName ->
            Toast.makeText(context, destinationName, Toast.LENGTH_LONG).show()
        })

        popularCottagesAdapter = PopularCottagesAdapter(CottageListener { cottage ->
            Toast.makeText(context, cottage.toString(), Toast.LENGTH_LONG).show()
            // handle popular cottage click
            viewModel.onPopularCottageClicked(cottage)
        })

        with(binding) {
            popularDestinationsList.adapter = popularDestinationsAdapter
            popularDestinationsList.addItemDecoration(HorizontalItemDecoration(32))

            popularCottagesList.adapter = popularCottagesAdapter
            popularCottagesList.addItemDecoration(HorizontalItemDecoration(32))
        }
    }

    private fun setObservers() {

        //istAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
        viewModel.popularCottages.observe(viewLifecycleOwner, {
            it?.let {
                popularCottagesAdapter.submitList(it.toList().reversed()) // pass a copy of the list to be diffed
            }
        })

        // When popular cottage is clicked navigate to cottage detail fragment
        viewModel.navigateToCottageDetail.observe(viewLifecycleOwner, { cottage ->
            cottage?.let {
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToCottageDetailFragment(cottage)
                )
                viewModel.onCottageDetailNavigated()
            }
        })

        // When search is clicked navigate to search fragment
        viewModel.navigateToSearch.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToSearchFragment()
                )
                viewModel.onSearchNavigated()
            }
        })
    }
}