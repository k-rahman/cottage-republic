package fi.oamk.cottagerepublic.data

import android.os.Parcelable
import fi.oamk.cottagerepublic.R
import kotlinx.parcelize.Parcelize


@Parcelize
data class Cottage (
    var cottageId: Long = 0L,
    var cottageLabel: String = "Test",
    var image: Int = R.drawable.ic_launcher_background,
    var rating: Float = 0.0F,
    var price: Float = 0.0F,
    var location: String = ""
) : Parcelable {
}