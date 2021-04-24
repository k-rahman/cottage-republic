package fi.oamk.cottagerepublic.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel(val total: Int) : ViewModel() {

    private var _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    fun onDoneClicked() {
        _navigateToHome.value = true
    }

    fun onHomeNavigated() {
        _navigateToHome.value = false
    }
}
