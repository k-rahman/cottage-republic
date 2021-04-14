package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding

class CreateCottageFragment : Fragment() {

    private lateinit var binding: FragmentCreateCottageBinding

//    class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            parent?.getItemAtPosition(position)
//        }
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            TODO("Not yet implemented")
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.v("createTest: ", "im created")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        val viewModel = ViewModelProvider(this).get(CreateCottageViewModel::class.java)

//        ArrayAdapter.createFromResource(
//            requireActivity(),
//            R.array.spinnerGuestAmount,
//            requireActivity().findViewById(R.id.spinnerGuestAmount))
//        .also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//            binding.spinnerGuestAmount.adapter = adapter
//
//        }

//        val spinner : Spinner = binding.spinnerGuestAmount
//        spinner.onItemSelectedListener = this



        binding.createViewModel = viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//        TODO("Not yet implemented")
//    }

}