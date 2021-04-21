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
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentHomeScreenBinding
import fi.oamk.cottagerepublic.repository.AuthRepository
import fi.oamk.cottagerepublic.util.HorizontalItemDecoration
import fi.oamk.cottagerepublic.util.Resource

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
//        val authRepository = AuthRepository()
//        authRepository.logOut()


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
        binding.lifecycleOwner = viewLifecycleOwner

        setHomeAdapters()
        setObservers()

        return binding.root
    }

    private fun setHomeAdapters() {
        popularDestinationsAdapter = PopularDestinationAdapter(DestinationListener {
            viewModel.onPopularDestinationClicked(it)
//            Toast.makeText(context, destinationName, Toast.LENGTH_LONG).show()
        })

        popularCottagesAdapter = PopularCottagesAdapter(CottageListener {
//
            // handle popular cottage click
            viewModel.onPopularCottageClicked(it)
        })

        with(binding) {
            popularDestinationsList.adapter = popularDestinationsAdapter
            popularDestinationsList.addItemDecoration(HorizontalItemDecoration(32))

            popularCottagesList.adapter = popularCottagesAdapter
            popularCottagesList.addItemDecoration(HorizontalItemDecoration(32))
        }
    }

    private fun setObservers() {

        viewModel.popularCottages.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                        binding.progressIndicator.show()
                }
                is Resource.Success -> {

                    // ListAdapter provides a method called submitList() to tell ListAdapter that a new version of the list is available.
                    var cottageList = it.data as MutableList<Cottage>
                    popularCottagesAdapter.submitList(
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

        viewModel.popularDestinations.observe(viewLifecycleOwner, {
            it?.let {
                popularDestinationsAdapter.submitList(it.toList())
            }
        })

        // When popular cottage is clicked navigate to cottage detail fragment
        viewModel.navigateToCottageDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToCottageDetailFragment(it)
                )
                viewModel.onCottageDetailNavigated()
            }
        })

        viewModel.navigateToSearchDestination.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToSearchFragment(it)
                )
                viewModel.onSearchDestinationNavigated()
            }
        })

        // When search is clicked navigate to search fragment
        viewModel.navigateToSearch.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToSearchFragment(null)
                )
                viewModel.onSearchNavigated()
            }
        })
    }
}