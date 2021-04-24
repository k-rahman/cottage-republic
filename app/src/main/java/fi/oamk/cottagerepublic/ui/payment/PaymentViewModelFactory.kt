package fi.oamk.cottagerepublic.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentViewModelFactory(private val total: Int) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java))
            return PaymentViewModel(total) as T

        throw IllegalArgumentException("Unknown class")
    }
}
