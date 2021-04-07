package fi.oamk.cottagerepublic.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.oamk.cottagerepublic.R

/**
 * A simple [Fragment] subclass.
 * Use the [MyCottagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyCottagesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cottages_screen, container, false)
    }


}