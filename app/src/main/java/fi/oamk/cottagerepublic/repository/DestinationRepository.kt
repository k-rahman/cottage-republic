package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import fi.oamk.cottagerepublic.data.Destination

class DestinationRepository(private val databaseReference: DatabaseReference) {

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: DestinationRepository? = null
        fun getInstance(databaseReference: DatabaseReference): DestinationRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = DestinationRepository(databaseReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun getPopularDestinations(): MutableLiveData<MutableList<Destination>> {
        val destination = MutableLiveData<MutableList<Destination>>()
        val list = mutableListOf<Destination>()

        databaseReference
            .get()
            .addOnSuccessListener { dataSnapshot ->
                destination.value = createDestinationList(list, dataSnapshot)
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        return destination
    }

    private fun createDestinationList(
        list: MutableList<Destination>,
        dataSnapshot: DataSnapshot
    ): MutableList<Destination> {
        for (snapShot in dataSnapshot.children) {
            val values = snapShot.value as HashMap<*, *>
            val newDestination = Destination()
            with(newDestination) {
                if (values["name"] != null)
                    name = values["name"].toString()
                if (values["image"] != null)
                    image = values["image"].toString()
            }
            list.add(newDestination)
        }
        return list
    }
}
