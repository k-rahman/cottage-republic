package fi.oamk.cottagerepublic.ui.cottageDetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCottageDetailScreenBinding
import fi.oamk.cottagerepublic.ui.calendar.CalendarFragment
import fi.oamk.cottagerepublic.util.MapUtils
import fi.oamk.cottagerepublic.util.VerticalItemDecoration


class CottageDetailFragment : Fragment() {
    private lateinit var binding: FragmentCottageDetailScreenBinding
    private lateinit var viewModelFactory: CottageDetailViewModelFactory
    private lateinit var viewModel: CottageDetailViewModel
    private lateinit var cottageImageAdapter: CottageImageAdapter
    private lateinit var amenitiesAdapter: AmenityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cottage_detail_screen, container, false)

        // Scoping a ViewModel to the Navigation Graph
        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModelFactory =
            CottageDetailViewModelFactory(CottageDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage)
        viewModel = ViewModelProvider(backStackEntry, viewModelFactory).get(CottageDetailViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_close_white_24)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        setCottageDetailAdapters()
        setObservers(savedInstanceState)

        return binding.root
    }

    private fun setCottageDetailAdapters() {
        cottageImageAdapter = CottageImageAdapter(CottageImageListener {
//            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        })
        binding.cottageImageSlider.adapter = cottageImageAdapter

        amenitiesAdapter = AmenityAdapter()
        binding.amenitiesList.adapter = amenitiesAdapter
        binding.amenitiesList.addItemDecoration(VerticalItemDecoration(32))
    }

    private fun setObservers(savedInstanceState: Bundle?) {

        val coordinatesList = arrayListOf<HashMap<String, Double>>()
        viewModel.selectedCottage.observe(viewLifecycleOwner, {
            it.let {
                cottageImageAdapter.submitList(it.images)
                amenitiesAdapter.submitList(it.amenities)

                if (!it.coordinates.isNullOrEmpty()) {
                    // initialize the map
                    coordinatesList.add(it.coordinates)
                    MapUtils.initializeMap(savedInstanceState, resources, binding.cottageMap, coordinatesList, false)
                }
            }
        })

        val calendarFragment = CalendarFragment()
        viewModel.showCalendar.observe(viewLifecycleOwner, {
            if (it) {
                this.parentFragmentManager.commit {
                    add(R.id.nav_host_fragment, calendarFragment)
                }

                // disable all actions in the background fragment, so it is not clickable
                binding.calendarButton.isEnabled = false
                binding.toolbar.navigationIcon = null

            } else {
                this.parentFragmentManager.commit {
                    remove(calendarFragment)
                }

                // disable all actions in the background fragment, so it is not clickable
                binding.calendarButton.isEnabled = true
                binding.toolbar.setNavigationIcon(R.drawable.icon_close_white_24)
            }
        })

        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    CottageDetailFragmentDirections.actionCottageDetailFragmentToMapFragment(
                        viewModel.selectedCottage.value!!
                    )
                )
                viewModel.onMapNavigated()
            }
        })

        viewModel.launchEmail.observe(viewLifecycleOwner, {
            if (it) {
                val email = Intent(Intent.ACTION_SEND)
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf("hostname@cottage-republic.com"))
                email.putExtra(Intent.EXTRA_SUBJECT, "Cottage for rent")
                email.putExtra(Intent.EXTRA_TEXT, "I have a question")

                //need this to prompts email client only
                email.type = "text/plain"
                startActivity(email)
            }
        })
    }

    //    override fun onResume() {
//        super.onResume()
//        binding.cottageMap.onResume()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        binding.cottageMap.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        binding.cottageMap.onStop()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        binding.cottageMap.onPause()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        binding.cottageMap.onLowMemory()
//    }
//
    override fun onDestroy() {
        super.onDestroy()
//        binding.cottageMap.onDestroy()

        // show bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.cottageMap.onDestroy()
//
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        binding.cottageMap.onSaveInstanceState(outState)
//    }
}