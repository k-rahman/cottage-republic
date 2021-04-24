package fi.oamk.cottagerepublic.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userId: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var cottages: MutableList<String> = mutableListOf(),
    var reservations: MutableList<String> = mutableListOf()
) : Parcelable