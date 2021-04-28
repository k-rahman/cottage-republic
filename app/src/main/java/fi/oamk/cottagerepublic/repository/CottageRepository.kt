package fi.oamk.cottagerepublic.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await

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

    suspend fun getAllCottages(): Resource<MutableList<Cottage>> {
        val cottages = mutableListOf<Cottage>()

        val dataSnapshot = databaseReference.get().await()

        for (cottage in dataSnapshot.children) {
            val values = cottage.value as HashMap<*, *>
            val cottage = createCottageObject(values)
            cottages.add(cottage)
        }

        return Resource.Success(cottages)
    }

    suspend fun getPopularCottages(limit: Int): Resource<MutableList<Cottage>> {
        val cottages = mutableListOf<Cottage>()

        val dataSnapshot = databaseReference
            .orderByChild("rating")
            .limitToLast(limit)
            .get().await()

        for (cottage in dataSnapshot.children) {
            val values = cottage.value as HashMap<*, *>
            val cottage = createCottageObject(values)
            cottages.add(cottage)
        }

        return Resource.Success(cottages)
    }

    suspend fun getCottageByKey(cottageKeys: List<String>): Resource<MutableList<Cottage>> {
        val userCottages = mutableListOf<Cottage>()

        cottageKeys.forEach {
            val snapshot = databaseReference.child(it).get().await()
            val values = snapshot.value as HashMap<Any, Any>
            val cottage = createCottageObject(values)
            userCottages.add(cottage)
        }

        return Resource.Success(userCottages)
    }

    suspend fun createNewCottage(cottage: Cottage): String {
        var key = databaseReference.push().key
        key = "cottage$key"
        cottage.cottageId = key.toString()

        val childUpdates = hashMapOf<String, Any>(
            "$key" to cottage,
        )

        databaseReference.updateChildren(childUpdates).await()

        return key
    }

    suspend fun updateCottageByCottageId(cottage: Cottage) {
        val childUpdates = hashMapOf(
            "${cottage.cottageId}/description" to cottage.description,
            "${cottage.cottageId}/guests" to cottage.guests,
            "${cottage.cottageId}/price" to cottage.price,
            "${cottage.cottageId}/cottageLabel" to cottage.cottageLabel,
            "${cottage.cottageId}/amenities" to cottage.amenities,
            "${cottage.cottageId}/coordinates" to cottage.coordinates,
            "${cottage.cottageId}/images" to cottage.images,
            "${cottage.cottageId}/location" to cottage.location,
        )
        databaseReference.updateChildren(childUpdates).await()
    }

    suspend fun deleteCottageById(cottageId: String) {
        databaseReference.child(cottageId).removeValue().await()
    }

    suspend fun uploadImages(images: ArrayList<Uri>) {
        for (image in images) {
            Log.v("imageurl:", image.toString())

            val metadata = storageMetadata {
                contentType = "image/jpeg"
            }

            storageReference.child("${image.lastPathSegment}").putFile(image, metadata).await()
        }
    }

    suspend fun getImagesUrl(images: ArrayList<Uri>): ArrayList<String> {
        val imagesUrl = arrayListOf<String>()

        images.forEach {
            imagesUrl.add(storageReference.child(it.lastPathSegment!!).downloadUrl.await().toString())
        }

        return imagesUrl
    }

    private fun createCottageObject(values: HashMap<*, *>): Cottage {
        val newCottage = Cottage()

        with(newCottage) {
            if (values["cottageId"] != null)
                cottageId = values["cottageId"].toString()

            if (values["hostId"] != null)
                hostId = values["hostId"].toString()

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
                guests = values["guests"].toString()

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
                    images.add(image)
        }
        return newCottage
    }
}