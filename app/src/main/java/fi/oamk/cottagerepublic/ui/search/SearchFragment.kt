package fi.oamk.cottagerepublic.ui.search

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = this

        setObservers()
        setSearchAdapter()
        setFocusOnSearchBar()
//        setSearchQueryListener()

        return binding.root
    }

    private fun setObservers() {
        viewModel.cottagesList.observe(viewLifecycleOwner, {
            if (searchAdapter.fullList.isEmpty())
                searchAdapter.fullList = it.toList()


            it?.let {
                searchAdapter.submitList(it.toList()) // pass a copy of the list to be diffed
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
            }
        })
    }

    private fun setSearchAdapter() {
        searchAdapter = SearchAdapter(SearchListListener { cottage ->
            Toast.makeText(context, cottage.toString(), Toast.LENGTH_LONG).show()
            // handle popular cottage click
            viewModel.onSearchItemClicked(cottage)
        })

        binding.searchList.adapter = searchAdapter
        binding.searchList.addItemDecoration(VerticalItemDecoration(32))
    }

//    private fun setSearchQueryListener() {
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(query: String?): Boolean {
////                viewModel.searchByLocation(newText)
////                if (searchAdapter.fullList.isNotEmpty())
////                    searchAdapter.filter.filter(query)
//                return false
//            }
//        })
//    }

    private fun setFocusOnSearchBar() {
        binding.searchView.requestFocus()
        val keyboard = context
            ?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}