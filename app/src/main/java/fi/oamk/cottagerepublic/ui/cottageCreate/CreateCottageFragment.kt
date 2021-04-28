package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding
import fi.oamk.cottagerepublic.util.MapUtils

@Suppress("UNCHECKED_CAST")
class CreateCottageFragment : Fragment() {
    private lateinit var binding: FragmentCreateCottageBinding
    private lateinit var viewModel: CreateCottageViewModel
    private lateinit var viewModelFactory: CreateCottageViewModelFactory
    private lateinit var disableBackClick: OnBackPressedCallback
    private var images: ArrayList<Uri> = arrayListOf()
    private var missingString = ""
    private val PICK_IMAGES_CODE = 0
    private var imageListPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //mapbox key
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))


        //set binding to xml
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        //navigation
        initToolbar()
        initViewModel()

        //binding for viewmodel
        binding.createViewModel = viewModel
        binding.lifecycleOwner = this

        initMap(savedInstanceState)
        disableBackButton()
        setObservers()

        //create an image arraylist, check if it already exists in viewmodel
        binding.imagesView.isVisible = false
        images = ArrayList()
        if (viewModel.newCottageImages != emptyList<Uri>()) {
            images = viewModel.newCottageImages
            displayImages()
        } else if (viewModel.newCottageImageNames.isNotEmpty()) {
            binding.imagesView.isVisible = true
            binding.confirmButtonCreateCottage.text = "Edit Cottage"
        }

        //image upload button
        binding.pickImageButton.setOnClickListener {
            pickImagesIntent(0)
        }

        return binding.root
    }

    //init toolbar
    private fun initToolbar() {
        // toolbar configuration
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)
        binding.toolbar.setNavigationIcon(R.drawable.icon_back_arrow_24)
    }

    private fun initViewModel() {
        // ViewModelProvider returns an existing ViewModel if one exists,
        // or it creates a new one if it does not already exist.
        val cottageArg = CreateCottageFragmentArgs.fromBundle(requireArguments()).cottage
        viewModelFactory = CreateCottageViewModelFactory(cottageArg)
        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)
        viewModel = ViewModelProvider(backStackEntry, viewModelFactory).get(CreateCottageViewModel::class.java)
    }

    private fun setObservers() {
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.toolbar.setNavigationOnClickListener {
                    it.isEnabled = false
                }
                disableBackClick.isEnabled = true
                binding.loadingLayout.root.visibility = View.VISIBLE
            }
        }

        //display error if not all required fields were filled in
        viewModel.fillInBoxes.observe(viewLifecycleOwner) {
            missingString = "The following are required:"
            for (missingField in it) {
                missingString = "$missingString $missingField*"
            }
            binding.errorTextView.text = missingString
        }

        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    CreateCottageFragmentDirections.actionCreateCottageFragmentToCreateCottageMapFragment(
                    )
                )
                viewModel.onMapNavigated()
            }
        })

        // Navigate back to MyCottages with newly created cottage
        viewModel.navigateToMyCottage.observe(viewLifecycleOwner) {
            if (it) {
                navigateToMyCottage()
            }
        }

        //display address
        viewModel.newCottageAddress.observe(viewLifecycleOwner) {
            binding.addessBox.text = it
        }

    }

    private fun initMap(savedInstanceState: Bundle?) {
        val mapUtils = MapUtils(savedInstanceState, requireContext(), binding.cottageMap)
        mapUtils.mapboxMap.observe(viewLifecycleOwner, {
            if (viewModel.cottageCoordinates.isNullOrEmpty())
                mapUtils.initCameraPosition(hashMapOf("lat" to 65.142455, "long" to 27.078449))
            else {
                mapUtils.updateMapStyle(viewModel.cottageCoordinates)
                viewModel.setAddress(
                    mapUtils.getPointAddress(viewModel.cottageCoordinates).getAddressLine(0)
                        .toString()
                )
            }
        })
    }

    //images functions
    private fun pickImagesIntent(imageNumber: Int) {
        val intent = Intent()
        intent.type = "image/+"
        //check if user clicked on image or button
        if (imageNumber > 0) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.action = Intent.ACTION_GET_CONTENT
            imageListPosition = imageNumber
            startActivityForResult(Intent.createChooser(intent, "select image"), PICK_IMAGES_CODE)
        } else {
            imageListPosition = 0
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select images"), PICK_IMAGES_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    var count = data.clipData!!.itemCount
                    if (count > 1) {
                        //picked multiple images
                        //clear the arrays if previous images were picked
//                        viewModel.newCottageImageNames.clear()
                        this.images.clear()
                        //get number of picker images

                        Log.v("count: ", count.toString())
                        if (count > 5)
                            count = 5
                        for (i in 0 until count) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            //add image to list
                            this.images.add(imageUri)
                            //Log.v("imageuri ",imageUri.toString() )
//                            viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())
                            Log.v("multiple ", "images have been picked")
                        }

                        //display image function
                        displayImages()

                    } else {
                        //picked single image
                        Log.v("Single ", "images have been picked")
                        val imageUri = data.data!!
                        //if user clicked on a previous image..
                        if (imageListPosition > 0) {
                            imageListPosition--
                            this.images[imageListPosition] = imageUri
                            Log.v("imageuri ", imageUri.toString())
//                            viewModel.newCottageImageNames[imageListPosition] =
//                                imageUri.lastPathSegment.toString()
                            displayImages()
                        } else {
                            //clear the arrays if previous images were picked
//                            viewModel.newCottageImageNames.clear()
                            this.images.clear()
                            this.images.add(imageUri)
                            Log.v("imageuri ", imageUri.toString())
//                            viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())
                            displayImages()
                        }
                    }
                }
            }
            //set viewmodel var to imagesarray
            viewModel.newCottageImages = images
        }
    }

    //the display image function
    private fun displayImages() {
        val count = this.images.size

        binding.imagesView.isVisible = true
        binding.pickImageButton.text = "Re-pick images"
        //set main image
        binding.mainImage.setImageURI(this.images[0])
        binding.mainImage.setOnClickListener {
            pickImagesIntent(1)
        }

        //set extra images depending on the count

        if (count >= 2) {
            binding.extraImage1.setImageURI(this.images[1])
            binding.extraImage1.setOnClickListener {
                pickImagesIntent(2)
            }
        } else {
            // binding.extraImage1.isVisible = false
            binding.extraImage1.setImageURI(null)
            binding.extraImage1.setOnLongClickListener(null)
        }
        if (count >= 3) {
            binding.extraImage2.setImageURI(this.images[2])
            binding.extraImage2.setOnClickListener {
                pickImagesIntent(3)
            }
        } else {
            binding.extraImage2.setImageURI(null)
            binding.extraImage2.setOnLongClickListener(null)
        }
        //  binding.extraImage2.isVisible = false
        if (count >= 4) {
            binding.extraImage3.setImageURI(this.images[3])
            binding.extraImage3.setOnClickListener {
                pickImagesIntent(4)
            }
        } else {
            binding.extraImage3.setImageURI(null)
            binding.extraImage3.setOnLongClickListener(null)
        }
        //  binding.extraImage3.isVisible = false
        if (count >= 5) {
            binding.extraImage4.setImageURI(this.images[4])
            binding.extraImage4.setOnClickListener {
                pickImagesIntent(5)
            }
        } else {
            binding.extraImage4.setImageURI(null)
            binding.extraImage4.setOnLongClickListener(null)
        }
        //   binding.extraImage4.isVisible = false
    }

    private fun navigateToMyCottage() {
        findNavController().navigate(R.id.accountCottageScreenFragment, null, getNavOptions())
        viewModel.onMyCottagesNavigated()
    }

    private fun getNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_left)
            .setExitAnim(R.anim.slide_out_right)
            .setPopEnterAnim(android.R.anim.slide_in_left)
            .setPopExitAnim(android.R.anim.slide_out_right)
            .setPopUpTo(R.id.accountCottageScreenFragment, true)
            .build()
    }

    private fun disableBackButton() {
        disableBackClick = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, disableBackClick)
    }
}