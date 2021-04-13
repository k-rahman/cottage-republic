package fi.oamk.cottagerepublic.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
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

    suspend fun getAllCottages(): MutableList<Cottage> {
        var cottagesList = mutableListOf<Cottage>()
        val dataSnapshot = databaseReference.get().await().children

        cottagesList = createCottageList(cottagesList, dataSnapshot)

        return cottagesList
    }

    suspend fun getPopularCottages(limit: Int): Resource<MutableList<Cottage>> {
        var cottagesList = mutableListOf<Cottage>()
        val dataSnapshot = databaseReference
            .orderByChild("rating")
            .limitToLast(limit)
            .get().await().children

        cottagesList = createCottageList(cottagesList, dataSnapshot)

        return Resource.Success(cottagesList)
    }

    private suspend fun createCottageList(
        cottagesList: MutableList<Cottage>,
        dataSnapshot: MutableIterable<DataSnapshot>
    ): MutableList<Cottage> {
        for (cottage in dataSnapshot) {
            val values = cottage.value as HashMap<*, *>
            val newCottage = Cottage()
            with(newCottage) {
                if (values["cottageId"] != null)
                    cottageId = values["cottageId"].toString().toLong()

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
            cottagesList.add(newCottage)
        }
        return cottagesList
    }
}