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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mapbox.mapboxsdk.Mapbox
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCreateCottageBinding
import fi.oamk.cottagerepublic.util.MapUtils

class CreateCottageFragment : Fragment() {
    private lateinit var binding: FragmentCreateCottageBinding
    private lateinit var viewModel: CreateCottageViewModel
    private var images: ArrayList<Uri> = arrayListOf()
    private var position = 0
    private val PICK_IMAGES_CODE = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        Log.v("createTest: ", "im created")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_cottage, container, false)

        val backStackEntry = findNavController().getBackStackEntry(R.id.CreateCottageFragment)

        viewModel = ViewModelProvider(backStackEntry).get(CreateCottageViewModel::class.java)

        viewModel.navigateToMap.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(
                    CreateCottageFragmentDirections.actionCreateCottageFragmentToCreateCottageMapFragment(
                    )
                )
                viewModel.onMapNavigated()
            }
        })

        images = ArrayList()


        binding.pickImageButton.setOnClickListener{
            pickImagesIntent()
        }


        binding.createViewModel = viewModel
        binding.lifecycleOwner = this

        val mapUtils = MapUtils(savedInstanceState, requireContext(), binding.cottageMap)

        mapUtils.mapboxMap.observe(viewLifecycleOwner, {
            if (viewModel.cottageCoordinates.isNullOrEmpty())
                mapUtils.initCameraPosition(hashMapOf("lat" to 65.142455, "long" to 27.078449))
            else
                mapUtils.updateMapStyle(viewModel.cottageCoordinates)
        })

        return binding.root
    }

    //images



    private fun pickImagesIntent(){
        val intent = Intent()
        intent.type = "image/+"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"select images"), PICK_IMAGES_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data!!.clipData != null)
                {
                    //picked multiple images
                    //get number of picker images
                    val count = data.clipData!!.itemCount
                    for(i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        //add image to list
                        images.add(imageUri)
                        viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())

                    }
                    //set first image from list to image switcher
                   // binding.imageSwitcher.setImageURI(this!!.images[0].toUri())
                    binding.mainImage.setImageURI(this.images[0])
                    if(this.images[1].toString().isNotEmpty()){
                        binding.extraImage1.setImageURI(this.images[1])
                    }
                    if(this.images[2].toString().isNotEmpty()){
                        binding.extraImage2.setImageURI(this.images[2])
                    }
                    if(this.images[3].toString().isNotEmpty()){
                        binding.extraImage3.setImageURI(this.images[3])
                    }
                    if(this.images[3].toString().isNotEmpty()){
                        binding.extraImage4.setImageURI(this.images[4])
                    }
                    position = 0
                }
                else
                {
                    //picked single image
                    val imageUri = data.data
                    //set image to iamge switcher
                   // binding.imageSwitcher.setImageURI(imageUri)
                    binding.mainImage.setImageURI(imageUri)
                    position = 0
                }
            }
            viewModel.newCottageImages = images
        }
    }


}