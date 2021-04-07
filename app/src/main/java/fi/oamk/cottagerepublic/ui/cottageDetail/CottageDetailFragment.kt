package fi.oamk.cottagerepublic.ui.cottageDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCottageDetailBinding

class CottageDetailFragment : Fragment() {
    private lateinit var binding: FragmentCottageDetailBinding
    private lateinit var viewModelFactory: CottageDetailViewModelFactory
    private lateinit var viewModel: CottageDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cottage_detail, container, false)

        viewModelFactory = CottageDetailViewModelFactory(CottageDetailFragmentArgs.fromBundle(requireArguments()).selectedCottage)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CottageDetailViewModel::class.java)

        binding.viewModel = viewModel

        return binding.root
    }
}