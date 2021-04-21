package fi.oamk.cottagerepublic.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    var location: HashMap<String, String> = hashMapOf(),
    var images:MutableList<String> = mutableListOf(),
) : Parcelable {
}