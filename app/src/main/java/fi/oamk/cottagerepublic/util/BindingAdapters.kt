package fi.oamk.cottagerepublic.util


import android.widget.*
import androidx.core.widget.addTextChangedListener
import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.squareup.picasso.Picasso
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.Cottage
import fi.oamk.cottagerepublic.data.Destination
import org.w3c.dom.Text


// destination bindings
@BindingAdapter("image")
fun setCottageImage(view: ImageView, item: Destination) {
    if (item.images.isNotEmpty())
        Picasso.get().load(item.images[0]).into(view)
}

@SuppressLint("SetTextI18n")
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
    if(Price.startsWith("0"))
    {
        text = Price.replaceFirst("^0+".toRegex(), "")

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
        listener?.onChange()
    }
}