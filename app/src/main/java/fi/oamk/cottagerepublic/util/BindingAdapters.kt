package fi.oamk.cottagerepublic.ui.home

import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
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

@BindingAdapter("imageUrl")
fun ImageView.setSliderImage(item: String) {
    setImageResource(Integer.valueOf(item))

}