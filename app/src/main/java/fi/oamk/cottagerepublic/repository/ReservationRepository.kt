package fi.oamk.cottagerepublic.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import fi.oamk.cottagerepublic.util.Resource
import kotlinx.coroutines.tasks.await

class ReservationRepository(private val databaseReference: DatabaseReference) {

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: ReservationRepository? = null
        fun getInstance(databaseReference: DatabaseReference): ReservationRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = ReservationRepository(databaseReference)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    fun createReservation(userId: String, cottageId: String, dates: List<String>) {
        var key = databaseReference.push().key
        key = "reservation${key}"

        val childUpdates = hashMapOf<String, Any>(
            "reservations/$key" to dates,
            "cottages/$cottageId/reservations/$key" to true,
            "users/$userId/reservations/$key" to true
        )

        databaseReference.updateChildren(childUpdates)
    }

    suspend fun getReservationsByCottageId(cottageId: String): Resource<List<String>> {
        val dataSnapshot = databaseReference.child("cottages")
            .child(cottageId).child("reservations")
            .get().await()

        val reservedDates = getReservations(dataSnapshot)

        return Resource.Success(reservedDates)
    }


//    suspend fun getReservationsByUserId(userId: String): Resource<List<String>> {
//        val dataSnapshot = databaseReference.child("reservations")
//            .child("$userId")
//            .get().await()
//
//        val reservedDates = createReservationList(dataSnapshot)
//
//        return Resource.Success(reservedDates)
//    }


    private suspend fun getReservations(snapshot: DataSnapshot): List<String> {
        val reservations = mutableListOf<String>()

        for (reservation in snapshot.children)
            reservations.add(reservation.key.toString())

        return createDatesList(reservations)
    }

    private suspend fun createDatesList(reservations: List<String>): List<String> {
        val reservedDates = mutableListOf<String>()

        for (reservation in reservations)
            databaseReference.child("reservations").child(reservation)
                .get().await().children.forEach { date ->
                        reservedDates.add(date.value.toString())
                }

        return reservedDates
    }
}