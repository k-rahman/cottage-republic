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
import fi.oamk.cottagerepublic.databinding.FragmentMapScreenBinding
import fi.oamk.cottagerepublic.util.MapUtils

class CottageMapFragment : Fragment() {
    private lateinit var binding: FragmentMapScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_screen, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initToolbar()
        initMap(savedInstanceState)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        return binding.root
    }

    private fun initToolbar() {
        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        val singleLocation = CottageMapFragmentArgs.fromBundle(requireArguments()).selectedCottageCoordinate.coordinates
        val mapUtils = MapUtils(savedInstanceState, requireContext(), binding.cottageMap, true)
        mapUtils.mapboxMap.observe(viewLifecycleOwner, {
            mapUtils.updateMapStyle(singleLocation)
        })
    }
}