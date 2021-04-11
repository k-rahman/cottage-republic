package fi.oamk.cottagerepublic.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentMapBinding
import fi.oamk.cottagerepublic.util.MapUtils

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this

        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        val locationsList = arrayListOf<HashMap<String, Double>>()
        val singleLocation = hashMapOf<String, Double>()
        singleLocation.put("long", 25.46816)
        singleLocation.put("lat", 65.01236)
        locationsList.add(singleLocation)
        MapUtils.initializeMap(savedInstanceState, resources, binding.cottageMap, locationsList, true)


        return binding.root
    }
}