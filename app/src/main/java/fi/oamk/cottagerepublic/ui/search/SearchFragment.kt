package fi.oamk.cottagerepublic.ui.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentSearchScreenBinding
import fi.oamk.cottagerepublic.util.Resource
import fi.oamk.cottagerepublic.util.VerticalItemDecoration

@Suppress("UNCHECKED_CAST")
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchScreenBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

        // if popular cottage clicked go directly to cottage details
        if (viewModel.cottage != null)
            navigateToCottageDetail(viewModel.cottage!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search_screen,
            container,
            false
        )

        initToolbar()

        // show navbar
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE

        binding.searchViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setObservers()
        setSearchAdapter()

        return binding.root
    }

    private fun initToolbar() {
        // toolbar configurations
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)
    }

    private fun initViewModel() {
        // ViewModelProvider returns an existing ViewModel if one exists,
        // or it creates a new one if it does not already exist.
        val destination = SearchFragmentArgs.fromBundle(requireArguments()).destination
        val cottage = SearchFragmentArgs.fromBundle(requireArguments()).cottage
        viewModelFactory = SearchViewModelFactory(cottage, destination)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.cottagesList.observe(viewLifecycleOwner, {
            setCottages(it)
        })

        viewModel.searchQuery.observe(viewLifecycleOwner, {
            searchAdapter.filter.filter(it)
        })

        viewModel.navigateToCottageDetail.observe(viewLifecycleOwner, { cottage ->
            cottage?.let {
                navigateToCottageDetail(it)
            }
        })

        viewModel.isSearchBarFocused.observe(viewLifecycleOwner, {
            toggleKeyboard(it)
        })
    }

    private fun setCottages(queryResult: Resource<Any>) {
        when (queryResult) {
            is Resource.Loading -> {
                binding.progressIndicator.show()
            }
            is Resource.Success -> {
                // if no args passed, show all cottages
                populateAdapters(queryResult.data as List<Cottage>)

                // focus search and show keyboard
                if (!viewModel.checkForPassedArgs()) {
                    binding.searchView.requestFocus()
                    viewModel.showKeyboard()
                }

                binding.progressIndicator.hide()
            }
            is Resource.Failure -> {
                Toast.makeText(
                    requireContext(),
                    "An error has occurred:${queryResult.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun populateAdapters(result: List<Cottage>) {
        // taking copy of the list to be filtered, submitting filtered list to the adapter in Filter
        // *searchQuery* value will be changed in any case
        searchAdapter.fullList = result.toList().sortedByDescending { it.rating }
    }

    private fun navigateToCottageDetail(selectedCottage: Cottage) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToCottageDetailFragment(selectedCottage)
        )
        viewModel.onCottageDetailNavigated()
        viewModel.closeKeyboard()
    }

    private fun toggleKeyboard(searchFocused: Boolean) {
        val keyboard = context
            ?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (searchFocused)
            keyboard.showSoftInput(binding.searchView, 1)
        else
            keyboard.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setSearchAdapter() {
        searchAdapter = SearchAdapter(SearchListListener { cottage ->
            // handle popular cottage click
            viewModel.onSearchItemClicked(cottage)
        })

        binding.searchList.adapter = searchAdapter
        binding.searchList.addItemDecoration(VerticalItemDecoration(32))
    }
}