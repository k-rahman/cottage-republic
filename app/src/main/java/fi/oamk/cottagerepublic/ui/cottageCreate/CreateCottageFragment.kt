package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailFragmentDirections
import fi.oamk.cottagerepublic.util.MapUtils

class CreateCottageFragment : Fragment() {

    private lateinit var binding: FragmentCreateCottageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        Log.v("createTest: ", "im created")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)

        val viewModel = ViewModelProvider(backStackEntry).get(CreateCottageViewModel::class.java)



        MapUtils.initializeCreateCottageMap(savedInstanceState,resources,binding.cottageMap, null)


        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    CreateCottageFragmentDirections.actionCreateCottageFragmentToCreateCottageMapFragment(

                    )
                )
                viewModel.onMapNavigated()
            }
        })

        binding.createViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }


}