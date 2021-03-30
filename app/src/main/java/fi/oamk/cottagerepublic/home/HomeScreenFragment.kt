package fi.oamk.cottagerepublic.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import fi.oamk.cottagerepublic.*
import fi.oamk.cottagerepublic.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Get a reference to the binding object and inflate the fragment views.
       binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_screen, container, false)

        val popularDestinationsAdapter = PopularDestinationAdapter(DestinationListener { destinationName ->
            Toast.makeText(context, destinationName, Toast.LENGTH_LONG).show()
        })

        val popularCottagesAdapter = PopularCottagesAdapter(CottageListener { cottageLabel ->
            Toast.makeText(context, cottageLabel, Toast.LENGTH_LONG).show()
        })

        with(binding) {
            popularDestinationsList.adapter = popularDestinationsAdapter
            popularDestinationsList.addItemDecoration(MarginItemDecoration(32))

            popularCottagesList.adapter = popularCottagesAdapter
            popularCottagesList.addItemDecoration(MarginItemDecoration(32))
        }

//        topAppBar.setNavigationOnClickListener {
//            // Handle navigation icon press
//        }
//
//        topAppBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.search -> {
//                    // Handle search icon press
//                    true
//                }
//                else -> false
//            }
//        }

            return binding.root
        }
}