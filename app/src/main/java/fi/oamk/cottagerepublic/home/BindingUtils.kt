package fi.oamk.cottagerepublic.home

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import fi.oamk.cottagerepublic.Cottage
import fi.oamk.cottagerepublic.Destination
import fi.oamk.cottagerepublic.auth.LoginScreenViewModel

@BindingAdapter("image")
fun ImageView.setDestinationImage(item: Destination) = setImageResource(item.image)


@BindingAdapter("destinationName")
fun TextView.setDestinationName(item: Destination) {
   text = item.destinationName
}

@BindingAdapter("image")
fun ImageView.setCottageImage(item: Cottage) = setImageResource(item.image)

@BindingAdapter("cottageLabel")
fun TextView.setCottageName(item: Cottage) {
   text = item.cottageLabel
}

@BindingAdapter("username")
fun EditText.writeUsername(item: LoginScreenViewModel)
{
   setText(item.username.value)
}