package fi.oamk.cottagerepublic.util

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout

object RegistrationBindingAdapters {

    @BindingAdapter("emailError", "email")
    @JvmStatic
    fun TextInputLayout.setEmail(error: String?, email: String?) {
        if (editText!!.hasFocus() && email.isNullOrEmpty())
            setError(null)
        else
            setError(error)

        if (error.isNullOrBlank())
            isErrorEnabled = false
    }

    @InverseBindingAdapter(attribute = "email")
    @JvmStatic
    fun TextInputLayout.getEmail(): String {
        return editText!!.text.toString()
    }

    @BindingAdapter("emailAttrChanged")
    @JvmStatic
    fun TextInputLayout.setEmailListener(listener: InverseBindingListener?) {
        editText!!.doOnTextChanged { text, start, before, count ->
            listener?.onChange()
        }

        editText!!.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                listener?.onChange()
        }
    }

    @BindingAdapter("passwordError", "password")
    @JvmStatic
    fun TextInputLayout.setPassword(error: String?, password: String?) {
        if (editText!!.hasFocus() && password.isNullOrEmpty())
            setError(null)
        else
            setError(error)

        if (error.isNullOrBlank())
            isErrorEnabled = false
    }

    @InverseBindingAdapter(attribute = "password")
    @JvmStatic
    fun TextInputLayout.getPassword(): String {
        return editText!!.text.toString()
    }

    @BindingAdapter("passwordAttrChanged")
    @JvmStatic
    fun TextInputLayout.setPasswordListener(listener: InverseBindingListener?) {
        editText!!.doOnTextChanged { text, start, before, count ->
            listener?.onChange()
        }

        editText!!.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                listener?.onChange()
        }
    }
}