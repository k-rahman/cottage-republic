package fi.oamk.cottagerepublic.util

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager2.widget.ViewPager2

object ImageSliderBindingAdapter {

    @BindingAdapter("currentImage")
    @JvmStatic
    fun ViewPager2.setImagePosition(item: Int) {
    }

    @InverseBindingAdapter(attribute = "currentImage")
    @JvmStatic
    fun ViewPager2.geImagePosition(): Int {
        return currentItem
    }

    @BindingAdapter("currentImageAttrChanged")
    @JvmStatic
    fun ViewPager2.setListener(listener: InverseBindingListener?) {
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                listener?.onChange()
            }
        })
    }
}