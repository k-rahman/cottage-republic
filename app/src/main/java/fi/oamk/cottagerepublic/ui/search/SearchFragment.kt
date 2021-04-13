package fi.oamk.cottagerepublic.ui.search

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentSearchScreenBinding
import fi.oamk.cottagerepublic.util.VerticalItemDecoration

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchScreenBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var searchAdapter: SearchAdapter

    @RequiresApi(Build.VERSION_CODES.O)
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

        // toolbar configurations
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)

        // ViewModelProvider returns an existing ViewModel if one exists,
        // or it creates a new one if it does not already exist.
        viewModelFactory = SearchViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = this

        setObservers()
        setSearchAdapter()

        return binding.root
    }

    private fun setObservers() {
        viewModel.cottagesList.observe(viewLifecycleOwner, {
            it?.let {
                searchAdapter.submitList(it.toList()) // pass a copy of the list to be diffed
                searchAdapter.fullList = it.toList()
            }

            if (!viewModel.checkForPassedArgs()) {
                binding.searchView.requestFocus()
                viewModel.showKeyboard()
            }
        })

        viewModel.searchQuery.observe(viewLifecycleOwner, {
            searchAdapter.filter.filter(it)
        })

        viewModel.navigateToCottageDetail.observe(viewLifecycleOwner, { cottage ->
            cottage?.let {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToCottageDetailFragment(cottage)
                )
                viewModel.onCottageDetailNavigated()
                viewModel.closeKeyboard()
            }
        })

        viewModel.isSearchBarFocused.observe(viewLifecycleOwner, {
            val keyboard = context
                ?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (it)
                keyboard.showSoftInput(binding.searchView, 1)
            else
                keyboard.hideSoftInputFromWindow(view?.windowToken, 0)
        })
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