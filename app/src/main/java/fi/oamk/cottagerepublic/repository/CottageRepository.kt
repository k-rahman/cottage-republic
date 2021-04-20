package fi.oamk.cottagerepublic.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    fun createNewCottage(cottage: Cottage){

        var key = databaseReference.child("cottages").push().key
        key = "cottage$key"
        uploadImages(cottage.images,key)
        cottage.cottageId = key.toString()
        databaseReference.child(key).setValue(cottage)
    }

    fun uploadImages(images: MutableList<String>, key: String)
    {
        for(uri in images)
        {
            val file = Uri.fromFile(File("path/to/mountains.jpg"))

            val metadata = storageMetadata {
                contentType = "image/jpeg"
            }

            val uploadTask = storageReference.child("images/${file.lastPathSegment}").putFile(file, metadata)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = (100.0 * bytesTransferred) / totalByteCount
                Log.d(TAG, "Upload is $progress% done")
            }.addOnPausedListener {
                Log.d(TAG, "Upload is paused")
            }.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                // Handle successful uploads on complete
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