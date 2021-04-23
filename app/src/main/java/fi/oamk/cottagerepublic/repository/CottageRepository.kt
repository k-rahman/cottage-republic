package fi.oamk.cottagerepublic.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await
import java.io.File
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

@Suppress("UNCHECKED_CAST")
class CottageRepository(
    private val databaseReference: DatabaseReference,
    private val storageReference: StorageReference
) {

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: CottageRepository? = null
        fun getInstance(databaseReference: DatabaseReference, storageReference: StorageReference): CottageRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = CottageRepository(databaseReference, storageReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun createNewCottage(cottage: Cottage, images: ArrayList<Uri>)
    : String
    {



        var key = databaseReference.push().key
        key = "cottage$key"
        uploadImages(images,key)
        cottage.cottageId = key.toString()
      //  databaseReference.child("cottages").child(key).setValue(cottage)
      //  databaseReference.child("users").child(cottage.hostId).child("cottages").push().key

        val childUpdates = hashMapOf<String, Any>(
            "$key" to cottage,
        )


        databaseReference.updateChildren(childUpdates)

        return key
    }

    private fun uploadImages(images: ArrayList<Uri>, key: String){
      //  var counter = 0
        for(image in images)
        {
            Log.v("imageurl:" , image.toString())
           // val file = image
            //Log.v("File", file.toString())
            val metadata = storageMetadata {
                contentType = "image/jpeg"
            }

            //val uploadTask = storageReference.putFile(file, metadata)
            val uploadTask = storageReference.child("cottages").child("${image.lastPathSegment}").putFile(image, metadata)
          //  counter++
            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = (100.0 * bytesTransferred) / totalByteCount
                Log.d(TAG, "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d(TAG, "Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
                Log.d(TAG, "Upload failed")
            }.addOnSuccessListener {
                // Handle successful uploads on complete
                Log.d(TAG, "Upload Succes!")
                // ...
            }

        }


    }


    suspend fun getAllCottages(): Resource<MutableList<Cottage>> {
        val dataSnapshot = databaseReference.get().await()

        val cottagesList = createCottageList(dataSnapshot)

        return Resource.Success(cottagesList)
    }

    suspend fun getPopularCottages(limit: Int): Resource<MutableList<Cottage>> {
        val dataSnapshot = databaseReference
            .orderByChild("rating")
            .limitToLast(limit)
            .get().await()

        val cottages = createCottageList(dataSnapshot)

        return Resource.Success(cottages)
    }

    private suspend fun createCottageList(dataSnapshot: DataSnapshot): MutableList<Cottage> {
        val cottages = mutableListOf<Cottage>()

        for (cottage in dataSnapshot.children) {
            val values = cottage.value as HashMap<*, *>
            val newCottage = Cottage()
            with(newCottage) {
                if (values["cottageId"] != null)
                    cottageId = values["cottageId"].toString()

                if (values["cottageLabel"] != null)
                    cottageLabel = values["cottageLabel"].toString()

                if (values["rating"] != null)
                    rating = values["rating"].toString().toFloat()

                if (values["location"] != null)
                    for (spot in values["location"] as HashMap<String, String>)
                        location[spot.key] = spot.value

                if (values["price"] != null)
                    price = values["price"].toString().toInt()

                if (values["guests"] != null)
                    guests = values["guests"].toString().toInt()

                if (values["amenities"] != null) {
                    amenities.clear()
                    for (amenity in values["amenities"] as ArrayList<String>)
                        amenities.add(amenity)
                }

                if (values["description"] != null)
                    description = values["description"].toString()

                if (values["coordinates"] != null)
                    for (coordinate in values["coordinates"] as HashMap<String, Double>)
                        coordinates[coordinate.key] = coordinate.value

                if (values["images"] != null)
                    for (image in values["images"] as ArrayList<String>)
                        images.add(storageReference.child(image).downloadUrl.await().toString())
            }
            cottages.add(newCottage)
        }
        return cottages
    }
}