package fi.oamk.cottagerepublic.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(private val spaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            // first item
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }

            left = spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}