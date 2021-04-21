package fi.oamk.cottagerepublic.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import fi.oamk.cottagerepublic.data.Destination
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await

@Suppress("UNCHECKED_CAST")
class DestinationRepository(
    private val databaseReference: DatabaseReference,
    private val storageReference: StorageReference
) {

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: DestinationRepository? = null
        fun getInstance(
            databaseReference: DatabaseReference,
            storageReference: StorageReference
        ): DestinationRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = DestinationRepository(databaseReference, storageReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    suspend fun getPopularDestinations(): Resource<MutableList<Destination>> {
        val dataSnapshot = databaseReference.get().await()

        val destinations = createDestinationList(dataSnapshot)

        return Resource.Success(destinations)
    }

    private suspend fun createDestinationList(
        dataSnapshot: DataSnapshot
    ): MutableList<Destination> {
        val destinations = mutableListOf<Destination>()

        for (snapShot in dataSnapshot.children) {
            val values = snapShot.value as HashMap<*, *>
            val newDestination = Destination()
            with(newDestination) {
                if (values["location"] != null)
                    for (spot in values["location"] as HashMap<String, String>)
                        location[spot.key] = spot.value

                if (values["images"] != null)
                    for (image in values["images"] as ArrayList<String>)
                        images.add(storageReference.child(image).downloadUrl.await().toString())
            }
            destinations.add(newDestination)
        }
        return destinations
    }
}