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
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding

class CreateCottageFragment : Fragment() {

    private lateinit var binding: FragmentCreateCottageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.v("createTest: ", "im created")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        val viewModel = ViewModelProvider(this).get(CreateCottageViewModel::class.java)



        binding.createViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }


}