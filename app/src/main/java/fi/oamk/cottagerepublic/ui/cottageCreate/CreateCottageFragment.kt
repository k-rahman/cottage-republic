package fi.oamk.cottagerepublic.ui.cottageCreate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding
import fi.oamk.cottagerepublic.ui.account.AccountCottageScreenFragmentDirections
import fi.oamk.cottagerepublic.util.MapUtils

@Suppress("UNCHECKED_CAST")
class CreateCottageFragment : Fragment() {
    private lateinit var binding: FragmentCreateCottageBinding
    private lateinit var viewModel: CreateCottageViewModel
    private lateinit var viewModelFactory: CreateCottageViewModelFactory
    private var images: ArrayList<Uri> = arrayListOf()
    private var missingString = ""
    private val PICK_IMAGES_CODE = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //mapbox key
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))


        //set binding to xml
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        //set context for shared viewmodel
//        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)
//
//        viewModel = ViewModelProvider(backStackEntry).get(CreateCottageViewModel::class.java)


        //navigation

        initToolbar()
        initViewModel()

        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    CreateCottageFragmentDirections.actionCreateCottageFragmentToCreateCottageMapFragment(
                    )
                )
                viewModel.onMapNavigated()
            }
        })

//        viewModel.navigateContinue.observe(viewLifecycleOwner, {
//            if (it) {
//                findNavController().navigateUp()
//
//                viewModel.onMapNavigated()
//            }
//        })

        // Navigate back to MyCottages with newly created cottage
        viewModel.navigateToMyCottage.observe(viewLifecycleOwner) { cottage ->
            cottage?.let {
                navigateToMyCottage(it)
                viewModel.onMapNavigated()
            }
        }

        //display error if not all required fields were filled in
        viewModel.fillInBoxes.observe(viewLifecycleOwner,{
            missingString = "The following are required:"
            for(missingField in it)
            {
                missingString = "$missingString $missingField*"
            }
            binding.errorTextView.text = missingString
        }
        )

        //display address
        viewModel.newCottageAddress.observe(viewLifecycleOwner,{
            binding.addessBox.text = it
        }
        )

        //create an image arraylist, check if it already exists in viewmodel
//        binding.imagesView.isVisible = false
        images = ArrayList()
        if (viewModel.newCottageImages != emptyList<Uri>())
        {
            images = viewModel.newCottageImages
            displayImages()
        }

        //image upload button
        binding.pickImageButton.setOnClickListener{
            pickImagesIntent()
        }

        //binding for viewmodel
        binding.createViewModel = viewModel
        binding.lifecycleOwner = this

        val mapUtils = MapUtils(savedInstanceState, requireContext(), binding.cottageMap)

        mapUtils.mapboxMap.observe(viewLifecycleOwner, {
            if (viewModel.cottageCoordinates.isNullOrEmpty())
                mapUtils.initCameraPosition(hashMapOf("lat" to 65.142455, "long" to 27.078449))
            else {
                mapUtils.updateMapStyle(viewModel.cottageCoordinates)
                viewModel.setAddress(mapUtils.getPointAddress(viewModel.cottageCoordinates).getAddressLine(0).toString())
            }
        })

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
        val cottage = CreateCottageFragmentArgs.fromBundle(requireArguments()).cottage
        viewModelFactory = CreateCottageViewModelFactory(cottage)
        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)
        viewModel = ViewModelProvider(backStackEntry, viewModelFactory).get(CreateCottageViewModel::class.java)
    }

    //images functions

    private fun pickImagesIntent(){
        val intent = Intent()
        intent.type = "image/+"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"select images"), PICK_IMAGES_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //clear the arrays if previous images were picked
        viewModel.newCottageImageNames.clear()
        this.images.clear()

        if (requestCode == PICK_IMAGES_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data!!.clipData != null) {
                    //picked multiple images
                    //get number of picker images
                    var count = data.clipData!!.itemCount
                    Log.v("count: ", count.toString())
                    if (count > 5)
                        count = 5
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        //add image to list
                        this.images.add(imageUri)
                        //Log.v("imageuri ",imageUri.toString() )
                        viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())

                    }

                    //display image function
                    displayImages()
//                    binding.mainImage.setImageURI(this.images[0])
//
//                    //set images depending on the count
//
//                    if(count >= 2){
//                        binding.extraImage1.setImageURI(this.images[1])
//                    }
//                    if(count >= 3){
//                        binding.extraImage2.setImageURI(this.images[2])
//                    }
//                    if(count >= 4){
//                        binding.extraImage3.setImageURI(this.images[3])
//                    }
//                    if(count >= 5){
//                        binding.extraImage4.setImageURI(this.images[4])
//                    }

                }
                else
                {
                    //picked single image
                    val imageUri = data.data!!
                    this.images.add(imageUri)
                    Log.v("imageuri ",imageUri.toString() )
                    viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())
                    binding.mainImage.setImageURI(imageUri)
                }
            }
            //set viewmodel var to imagesarray
            viewModel.newCottageImages = images
        }
    }

    //the display image function
    private fun displayImages()

    {
        val count = this.images.size

        binding.imagesView.isVisible = true

        binding.mainImage.setImageURI(this.images[0])

        //set images depending on the count

        if(count >= 2){
            binding.extraImage1.setImageURI(this.images[1])
        }
        else
         binding.extraImage1.setImageURI(null)
        if(count >= 3){
            binding.extraImage2.setImageURI(this.images[2])
        }
        else
            binding.extraImage2.setImageURI(null)
        if(count >= 4){
            binding.extraImage3.setImageURI(this.images[3])
        }
        else
            binding.extraImage3.setImageURI(null)
        if(count >= 5){
            binding.extraImage4.setImageURI(this.images[4])
        }
        else
            binding.extraImage4.setImageURI(null)
    }

    private fun navigateToMyCottage(cottage: Cottage) {
        // Pass the new cottage as an argument back to MyCottages Screen
        findNavController().navigate(
            CreateCottageFragmentDirections.actionCreateCottageFragmentToAccountCottageScreenFragment(cottage)
        )
        viewModel.onMyCottageNavigated()
    }
}