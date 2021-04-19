package fi.oamk.cottagerepublic.ui.cottageDetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentCottageDetailScreenBinding
import fi.oamk.cottagerepublic.ui.calendar.CottageCalendarFragment
import fi.oamk.cottagerepublic.util.MapUtils
import fi.oamk.cottagerepublic.util.VerticalItemDecoration

class CottageDetailFragment : Fragment() {
    private lateinit var binding: FragmentCottageDetailScreenBinding
    private lateinit var viewModelFactory: CottageDetailViewModelFactory
    private lateinit var viewModel: CottageDetailViewModel
    private lateinit var cottageImageAdapter: CottageImageAdapter
    private lateinit var amenitiesAdapter: AmenityAdapter
    private lateinit var navigateUp: OnBackPressedCallback
    private lateinit var calendarHide: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // required by MapBox
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cottage_detail_screen, container, false)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        initToolbar()
        initViewModel()
        addCalendarFragment()
        initCottageDetailAdapters()
        setObservers(savedInstanceState)
        initBackPressCallBacks()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun initToolbar() {
        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_close_white_24)
    }

    private fun initViewModel() {
        // Scoping a ViewModel to the Navigation Graph
        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModelFactory =
            CottageDetailViewModelFactory(CottageDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage)
        viewModel = ViewModelProvider(backStackEntry, viewModelFactory).get(CottageDetailViewModel::class.java)
    }

    private fun addCalendarFragment() {
        if (parentFragmentManager.findFragmentByTag("calendar") == null) {
            val calendarFragment = CottageCalendarFragment()
            this.parentFragmentManager.commit {
                add(R.id.nav_host_fragment, calendarFragment, "calendar")
                hide(calendarFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun initCottageDetailAdapters() {
        cottageImageAdapter = CottageImageAdapter(CottageImageListener {
//            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        })

        binding.cottageImageSlider.adapter = cottageImageAdapter

        amenitiesAdapter = AmenityAdapter()
        binding.amenitiesList.adapter = amenitiesAdapter

        binding.amenitiesList.addItemDecoration(VerticalItemDecoration(32))
    }

    private fun setObservers(savedInstanceState: Bundle?) {

        viewModel.selectedCottage.observe(viewLifecycleOwner, {
            it.let {
                populateAdapters(it)
                initMap(savedInstanceState, it)
            }
        })

        viewModel.showCalendar.observe(viewLifecycleOwner, {
            // when back button pressed hide calendar, if calendar is hidden, navigate up
            navigateUp.isEnabled = !it
            calendarHide.isEnabled = it

            toggleUIWithCalendar(it)
        })

        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) navigateToMap()
        })

        viewModel.launchEmail.observe(viewLifecycleOwner, {
            if (it) {
                askTheHost()
            }
        })
    }

    private fun initMap(savedInstanceState: Bundle?, cottage: Cottage) {
        if (!cottage.coordinates.isNullOrEmpty()) {
            // initialize the map
            val mapUtils = MapUtils(savedInstanceState, requireContext(), binding.cottageMap, false)
            mapUtils.mapboxMap.observe(viewLifecycleOwner, {
                mapUtils.updateMapStyle(cottage.coordinates)
            })
        }
    }

    private fun populateAdapters(cottage: Cottage) {
        cottageImageAdapter.submitList(cottage.images)
        amenitiesAdapter.submitList(cottage.amenities)
    }

    private fun toggleUIWithCalendar(showCalendar: Boolean) {
        binding.calendarButton.isEnabled = !showCalendar

        if (showCalendar)
        // disable all actions in the background fragment, so it is not clickable
            binding.toolbar.navigationIcon = null
        else
        // enable actions in the background fragment
            binding.toolbar.setNavigationIcon(R.drawable.icon_close_white_24)
    }

    private fun initBackPressCallBacks() {
        // the OnBackPressedDispatcher controls how Back button events are dispatched to one or more OnBackPressedCallback objects.
        // this force user to use close button for calendar and not the back button
        calendarHide = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                viewModel.calendarHide()
            }
        }

        navigateUp = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, navigateUp)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, calendarHide)
    }

    private fun navigateToMap() {
        findNavController().navigate(
            CottageDetailFragmentDirections.actionCottageDetailFragmentToMapFragment(
                viewModel.selectedCottage.value!!
            )
        )
        viewModel.onMapNavigated()
    }

    private fun askTheHost() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("hostname@cottage-republic.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Cottage for rent")
        email.putExtra(Intent.EXTRA_TEXT, "I have a question")

        //need this to prompts email client only
        email.type = "text/plain"
        startActivity(email)

        viewModel.onEmailLaunched()
    }
}