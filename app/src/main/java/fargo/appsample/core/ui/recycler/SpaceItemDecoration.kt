package fargo.appsample.core.ui.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(
    private val top: Int,
    private val bottom: Int,
    private val right: Int,
    private val left: Int,
    private val between: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (position == 0) top else between
        outRect.bottom = if (position == parent.childCount - 1) bottom else 0
        outRect.right = right
        outRect.left = left
    }

}

fun RecyclerView.setSpacing(ver: Int, hor: Int, between: Int) {
    addItemDecoration(SpaceItemDecoration(ver, ver, hor, hor, between))
}