package fi.oamk.cottagerepublic.data

import android.os.Parcelable
import fi.oamk.cottagerepublic.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    var location: HashMap<String, String> = hashMapOf(),
    var image: String = R.drawable.cottage_image_sample2.toString()
) : Parcelable {
}