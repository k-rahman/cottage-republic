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
    private var missingString = ""
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

        viewModel.fillInBoxes.observe(viewLifecycleOwner,{
            for(missingField in it)
            {
                missingString = missingString + " $missingField "
            }
            binding.errorTextView.text = missingString
        }
        )

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

                        Log.v("imageuri ",imageUri.toString() )
                        viewModel.newCottageImageNames.add(imageUri.lastPathSegment.toString())

                    }


                    binding.mainImage.setImageURI(this.images[0])

                    if(count >= 2){
                        binding.extraImage1.setImageURI(this.images[1])
                    }
                    if(count >= 3){
                        binding.extraImage2.setImageURI(this.images[2])
                    }
                    if(count >= 4){
                        binding.extraImage3.setImageURI(this.images[3])
                    }
                    if(count >= 5){
                        binding.extraImage4.setImageURI(this.images[4])
                    }

                }
                else
                {
                    //picked single image
                    val imageUri = data.data
                    binding.mainImage.setImageURI(imageUri)
                }
            }
            viewModel.newCottageImages = images
        }
    }


}