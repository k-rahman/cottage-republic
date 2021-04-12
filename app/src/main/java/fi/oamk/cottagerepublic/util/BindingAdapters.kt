package fi.oamk.cottagerepublic.ui.home

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage

@BindingAdapter("image")
fun ImageView.setDestinationImage(item: Destination) = setImageResource(item.image)


@BindingAdapter("destinationName")
fun TextView.setDestinationName(item: Destination) {
    text = item.destinationName
}

@BindingAdapter("image")
fun ImageView.setCottageImage(item: Cottage) {
    if (item.images.isNotEmpty())
        setImageResource(Integer.valueOf(item.images[0]))
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
    text = item.location
}

@BindingAdapter("price")
fun TextView.setCottagePrice(item: Cottage) {
    text = "${item.price} €/night"
}


// cottage details binding
@BindingAdapter("imageUrl")
fun ImageView.setSliderImage(item: String) {
    setImageResource(Integer.valueOf(item))
}

@BindingAdapter("selectedCottagePrice")
fun TextView.setPrice(item: Int) {
    text = "$item €/ night"
}
@BindingAdapter("selectedCottageGuests")
fun TextView.setGuests(item: Int) {
    text = item.toString()
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



// calendar bindings
@BindingAdapter("numberOfNights")
fun TextView.setNumberOfNights(item: Int) {
    if (item == 0) {
        text = "1 day"
        return
    }

   text = "$item night(s)"
}