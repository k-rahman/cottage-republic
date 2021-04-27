package fi.oamk.cottagerepublic.util


import android.annotation.SuppressLint
import android.location.Address
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.squareup.picasso.Picasso
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination
import fi.oamk.cottagerepublic.data.User


// destination bindings
@BindingAdapter("image")
fun setCottageImage(view: ImageView, item: Destination) {
    if (item.images.isNotEmpty())
        Picasso.get().load(item.images[0]).into(view)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("location")
fun TextView.setDestinationName(item: Destination) {
    text = "${item.location["city"] ?: "Unknown city"}, ${item.location["country"] ?: "Unknown country"}"
}

// cottage bindings
@BindingAdapter("image")
fun setCottageImage(view: ImageView, item: Cottage) {
    if (item.images.isNotEmpty())
        Picasso.get().load(item.images[0]).into(view)
}

@BindingAdapter("cottageLabel")
fun TextView.setCottageName(item: Cottage) {
    text = item.cottageLabel
}

@BindingAdapter("rating")
fun RatingBar.setCottageRating(item: Cottage) {
    rating = item.rating
}

@SuppressLint("SetTextI18n")
@BindingAdapter("location")
fun TextView.setCottageLocation(item: Cottage) {
    text = "${item.location["city"] ?: "Unknown city"}, ${item.location["country"] ?: "Unknown country"}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("address")
fun TextView.setCottageAddress(address: Address?) {
    if (address != null)
        text =
            "${address.locality ?: "Unknown city"}, ${address.thoroughfare ?: "Unknown area"} , ${address.countryName ?: "Unknown country"}"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("price")
fun TextView.setCottagePrice(item: Cottage) {
    text = "${item.price} €/night"
}

@BindingAdapter("imageUrl")
fun setSliderImage(view: ImageView, item: String) {
    Picasso.get().load(item).into(view)
}


@BindingAdapter("imageCount", "imagePosition")
fun TextView.setItemCount(count: Int, position: Int) {
    text = "$position / $count"
}

@BindingAdapter("guestsNumber")
fun TextView.setGuests(item: Cottage) {
    text = item.guests.removeSuffix("Guest").removeSuffix("Guests").trim()
}

@BindingAdapter("amenityText")
fun TextView.setAmenityText(item: String) {
    text = item
}

@BindingAdapter("amenityIcon")
fun ImageView.setAmenityIcon(item: String) {
    when (item) {
        "sauna" -> setImageResource(R.drawable.icon_sauna_32)
        "hottub" -> setImageResource(R.drawable.icon_hottub_32)
        "kitchen" -> setImageResource(R.drawable.icon_kitchen_32)
        "pets" -> setImageResource(R.drawable.icon_pets_32)
        "smoking" -> setImageResource(R.drawable.icon_smoking_24)
        "power" -> setImageResource(R.drawable.icon_power_24)
        "water" -> setImageResource(R.drawable.icon_water_drop_24)
        "boat" -> setImageResource(R.drawable.icon_rowing_24)
        "fireplace" -> setImageResource(R.drawable.icon_fireplace_24)
        "grill" -> setImageResource(R.drawable.icon_outdoor_grill_24)
    }
}

@BindingAdapter("description")
fun TextView.setDescription(item: Cottage) {
    text = item.description
}

@BindingAdapter("hostName")
fun TextView.setHostName(item: User) {
    text = if (item.firstName.isBlank()) "Unknown host" else "${item.firstName} ${item.lastName}"
}

// calendar bindings
@BindingAdapter("numberOfNights")
fun TextView.setNumberOfNights(item: Int) {
    text = if (item == 0) {
        "-"
    } else
        "$item night(s)"
}

@BindingAdapter("numberOfNightNote")
fun TextView.setNumberOfNightsNote(item: Int) {
    visibility = if (item == 0)
        View.VISIBLE
    else
        View.INVISIBLE
}

@BindingAdapter("moreThanOneNight")
fun Button.setBookEnabled(item: Int) {
    isEnabled = item != 0
}

// booking binding
@BindingAdapter("priceOfNight", "numberOfNights")
fun TextView.setPriceByNumberOfNights(price: Int, numberOfNights: Int) {
    text = "$price € x $numberOfNights night(s)"
}

@BindingAdapter("taxes")
fun TextView.setTaxesAmount(taxes: Int) {
    text = "$taxes €"
}

@BindingAdapter("total")
fun TextView.setTotalBeforeTaxes(total: Int) {
    text = "$total €"
}

@BindingAdapter("CottagePrice")
fun TextView.setPrice(Price: String) {
//    if (Price != text.toString())
//        text = Price.replaceFirst("^0+".toRegex(), "")
    if (text.toString() != Price) {
        if (Price.startsWith("0")) {
            text = Price.replaceFirst("^0".toRegex(), "")
        } else {
            text = Price
        }
    }
}

@InverseBindingAdapter(attribute = "CottagePrice")
fun TextView.getPrice(): String {
    return text.toString()
    //.toString().replaceFirst("^0+".toRegex(), "")
}

@BindingAdapter("CottagePriceAttrChanged")
fun TextView.priceChanged(listener: InverseBindingListener) {
    addTextChangedListener {
        listener.onChange()
    }
}

// login
@BindingAdapter("username")
fun TextView.setUserName(username: String) {
    text = username
}

@InverseBindingAdapter(attribute = "username")
fun TextView.getUserName(): String {
    return text.toString()
}

@BindingAdapter("username")
fun TextView.onUsernameChanged(listener: InverseBindingListener) {
    addTextChangedListener {
        listener.onChange()
    }
}

@BindingAdapter("password")
fun TextView.setPassword(password: String) {
    text = password
}

@InverseBindingAdapter(attribute = "password")
fun TextView.getPassword(): String {
    return text.toString()
}

@BindingAdapter("password")
fun TextView.onPasswordChanged(listener: InverseBindingListener) {
    addTextChangedListener {
        listener.onChange()
    }
}

@BindingAdapter("cottageImage0")
fun setCottageImageZero(view: ImageView, item: Cottage?) {
    if (item != null && item.images.size > 0)
        Picasso.get().load(item.images[0]).into(view)
}

@BindingAdapter("cottageImage1")
fun setCottageImageONe(view: ImageView, item: Cottage?) {
    if (item != null && item.images.size > 1)
        Picasso.get().load(item.images[1]).into(view)
}

@BindingAdapter("cottageImage2")
fun setCottageImageTwo(view: ImageView, item: Cottage?) {
    if (item != null && item.images.size > 2)
        Picasso.get().load(item.images[2]).into(view)
}

@BindingAdapter("cottageImage3")
fun setCottageImageThree(view: ImageView, item: Cottage?) {
    if (item != null && item.images.size > 3)
        Picasso.get().load(item.images[3]).into(view)
}

@BindingAdapter("cottageImage4")
fun setCottageImageFour(view: ImageView, item: Cottage?) {
    if (item != null && item.images.size > 4)
        Picasso.get().load(item.images[4]).into(view)
}

//@BindingAdapter("firstName","lastName")
//fun TextView.setFirstLastName(firstName: String?, lastName: String?) {
//    text = "$firstName $lastName"
//}