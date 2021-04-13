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
                top = 0
            }

            left = spaceHeight
            right = spaceHeight
            bottom = spaceHeight

            if (parent.getChildAdapterPosition(view) == parent.layoutManager?.itemCount?.minus(1)) {
                bottom = 0
            }
        }
    }
}