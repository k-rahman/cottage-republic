package fi.oamk.cottagerepublic.util

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination

// destination bindings
@BindingAdapter("image")
fun ImageView.setDestinationImage(item: Destination) = setImageResource(item.image.toInt())

@BindingAdapter("location")
fun TextView.setDestinationName(item: Destination) {
    text = "${item.location["city"]}, ${item.location["country"]}"
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

@BindingAdapter("location")
fun TextView.setCottageLocation(item: Cottage) {
    text = "${item.location["city"]}, ${item.location["country"]}"
}

@BindingAdapter("price")
fun TextView.setCottagePrice(item: Cottage) {
    text = "${item.price} €/night"
}

@BindingAdapter("imageUrl")
fun setSliderImage(view: ImageView, item: String) {
    Picasso.get().load(item).into(view)
}

@BindingAdapter("guestsNumber")
fun TextView.setGuests(item: Cottage) {
    text = item.guests.toString()
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
    }
}

@BindingAdapter("description")
fun TextView.setDescription(item: Cottage) {
    text = item.description
}

// calendar bindings
@BindingAdapter("numberOfNights")
fun TextView.setNumberOfNights(item: Int) {
    if (item == 0) {
        text = "1 day"
        return
    }

    text = "$item night(s)"
}