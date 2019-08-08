package fargo.appsample.core.ui.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val onLoadMoreItems: () -> Unit
) : RecyclerView.OnScrollListener() {
    var isEnabled: Boolean = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (isEnabled) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                onLoadMoreItems()
            }
        }
    }
}