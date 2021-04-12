package fi.oamk.cottagerepublic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fi.oamk.cottagerepublic.data.Cottage

@Suppress("UNCHECKED_CAST")
class CottageRepository(private val databaseReference: DatabaseReference) {

    private lateinit var listener: ValueEventListener

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: CottageRepository? = null
        fun getInstance(databaseReference: DatabaseReference): CottageRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = CottageRepository(databaseReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun getAllCottages(): MutableLiveData<MutableList<Cottage>> {
        val cottages = MutableLiveData<MutableList<Cottage>>()
        val list = mutableListOf<Cottage>()

        listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cottages.value = createCottageList(list, dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Cottages failed, log a message
                Log.w("CottageRepository:onCancelled", databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(listener)
        return cottages
    }

    fun getPopularCottages(limit: Int): MutableLiveData<MutableList<Cottage>> {
        val cottages = MutableLiveData<MutableList<Cottage>>()
        val list = mutableListOf<Cottage>()

        databaseReference
            .orderByChild("rating")
            .limitToLast(limit)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                cottages.value = createCottageList(list, dataSnapshot)
            }
            .addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        return cottages
    }

    private fun createCottageList(list: MutableList<Cottage>, dataSnapshot: DataSnapshot): MutableList<Cottage> {
        for (snapShot in dataSnapshot.children) {
            val values = snapShot.value as HashMap<*, *>
            val newCottage = Cottage()
            with(newCottage) {
                if (values["cottageId"] != null)
                    cottageId = values["cottageId"].toString().toLong()
                if (values["cottageLabel"] != null)
                    cottageLabel = values["cottageLabel"].toString()
                if (values["rating"] != null)
                    rating = values["rating"].toString().toFloat()
                if (values["location"] != null)
                    location = values["location"].toString()
                if (values["price"] != null)
                    price = values["price"].toString().toInt()
                if (values["guests"] != null)
                    guests = values["guests"].toString().toInt()
                if (values["amenities"] != null) {
                    amenities.clear()
                    for (amenity in values["amenities"] as ArrayList<String>) {
                        amenities.add(amenity)
                    }
                }
                if (values["description"] != null) {
                    description = values["description"].toString()
                }
                if (values["coordinates"] !=null)
                   for (coordination in values["coordinates"] as HashMap<String, Double>) {
                       coordinates[coordination.key]= coordination.value
                   }
            }
            list.add(newCottage)
        }
        return list
    }

    fun removeListener() {
        databaseReference.removeEventListener(listener)
    }

//    fun searchByLocation(query: String?): MutableLiveData<MutableList<Cottage>> {
//        databaseReference
//            .orderByChild("location")
//            .startAt(query)
//            .endAt(query + "\uf8ff")
//            .get()
//            .addOnSuccessListener { dataSnapshot ->
//                cottages.value = createCottageList(dataSnapshot)
//            }
//            .addOnFailureListener {
//                Log.e("firebase", "Error getting data", it)
//            }
//
//        return cottages
//    }
}