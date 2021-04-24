package fi.oamk.cottagerepublic.ui.map

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
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentMapBinding
import fi.oamk.cottagerepublic.ui.cottageCreate.CreateCottageViewModel
import fi.oamk.cottagerepublic.util.MapUtils


class CreateCottageMapFragment : Fragment(), MapboxMap.OnMapClickListener {
    private lateinit var binding: FragmentMapBinding
    private lateinit var viewModel: CreateCottageViewModel
    private lateinit var mapUtils: MapUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this

        //init viewModel and binding
        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)
        viewModel = ViewModelProvider(backStackEntry).get(CreateCottageViewModel::class.java)
        binding.createViewModel = viewModel

        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        mapUtils = MapUtils(
            savedInstanceState,
            requireContext(),
            binding.cottageMap,
            true,
            this@CreateCottageMapFragment,
        )

        mapUtils.mapboxMap.observe(viewLifecycleOwner, {
            mapUtils.initCameraPosition(hashMapOf("lat" to 65.142455, "long" to 27.078449))
        })

        return binding.root
    }

    override fun onMapClick(point: LatLng): Boolean {
        mapUtils.mapboxMap.value?.getStyle {
            // Update the SymbolLayer icon's source to move the map pin
            val geoJsonSource = it.getSourceAs<GeoJsonSource>(MapUtils.SOURCE_ID)
            geoJsonSource?.setGeoJson(Feature.fromGeometry(Point.fromLngLat(point.longitude, point.latitude)))
        }

        viewModel.cottageCoordinates =
            hashMapOf("long" to point.longitude, "lat" to point.latitude)


        findNavController().popBackStack()
        return true
    }
}



