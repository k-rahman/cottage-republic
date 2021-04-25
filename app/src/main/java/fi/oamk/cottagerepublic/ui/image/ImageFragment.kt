package fi.oamk.cottagerepublic.ui.image

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
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentImageScreenBinding
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailViewModel

class ImageFragment : Fragment() {
    private lateinit var binding: FragmentImageScreenBinding
    private lateinit var viewModel: CottageDetailViewModel
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_screen, container, false)

        // hide bottom navigation
        requireActivity().findViewById<View>(R.id.bottom_nav_view).visibility = View.GONE

        initToolbar()
        initViewModel()
        initImageSliderAdapter()
        populateAdapters(viewModel.selectedCottage.value!!)
        setObservers()

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun initToolbar() {
        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)
    }

    private fun initViewModel() {
        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModel = ViewModelProvider(backStackEntry).get(CottageDetailViewModel::class.java)
        binding.cottageDetailViewModel = viewModel
    }

    private fun setObservers() {
        viewModel.currentImage.observe(viewLifecycleOwner, {
            // currentImage value will be set to currentItem, if viewPager2 currentItem is different
            if (it != binding.cottageImageSlider.currentItem)
                binding.cottageImageSlider.setCurrentItem(it, false)
        })
    }

    private fun initImageSliderAdapter() {
        imageAdapter = ImageAdapter()
        binding.cottageImageSlider.adapter = imageAdapter
    }

    private fun populateAdapters(cottage: Cottage) {
        imageAdapter.submitList(cottage.images)
    }
}