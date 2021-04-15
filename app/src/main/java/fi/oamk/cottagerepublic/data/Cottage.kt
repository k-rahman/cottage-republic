package fi.oamk.cottagerepublic.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cottage (
    var cottageId: String = "",
    var cottageLabel: String = "Cottage for rent",
    var images:MutableList<String> = mutableListOf(),
    var rating: Float = 0.0F,
    var price: Int = 0,
    var location: HashMap<String, String> = hashMapOf(),
    var guests: Int = 0,
    var amenities: MutableList<String> = mutableListOf(),
    var description: String = "Cottage for rent",
    var coordinates: HashMap<String, Double> = hashMapOf()
) : Parcelable {
}