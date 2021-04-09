package fi.oamk.cottagerepublic.ui.cottageDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.Style
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCottageDetailScreenBinding


class CottageDetailFragment : Fragment() {
    private lateinit var binding: FragmentCottageDetailScreenBinding
    private lateinit var viewModelFactory: CottageDetailViewModelFactory
    private lateinit var viewModel: CottageDetailViewModel
    private lateinit var cottageImageAdapter: CottageImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cottage_detail_screen, container, false)

        viewModelFactory =
            CottageDetailViewModelFactory(CottageDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CottageDetailViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_close_24)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        // get the map
        initializeMap(savedInstanceState)

        cottageImageAdapter = CottageImageAdapter(CottageImageListener {
//            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        })
        binding.cottageImageSlider.adapter = cottageImageAdapter

        viewModel.selectedCottage.observe(viewLifecycleOwner, {
            it.let {
                cottageImageAdapter.submitList(it.images)
            }
        })

        return binding.root
    }

    private fun initializeMap(savedInstanceState: Bundle?) {
        binding.cottageMap.onCreate(savedInstanceState)
        binding.cottageMap.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                // Map is set up and the style has loaded. Now you can add data or make other map adjustments
                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(5.0))
//                val uiSettings = mapboxMap.uiSettings
////                uiSettings.setAllGesturesEnabled(false)
////                uiSettings.isCompassEnabled = false
//                uiSettings.isAttributionEnabled = false
////                uiSettings.isLogoEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // show bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.VISIBLE
    }
}