package fargo.appsample.feature.pagination.ui

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import fargo.appsample.R
import fargo.appsample.core.store.MsgConsumer
import fargo.appsample.core.ui.recycler.PaginationScrollListener
import fargo.appsample.core.ui.recycler.adapter.AdapterDelegate
import fargo.appsample.core.ui.recycler.adapter.adapter
import fargo.appsample.core.ui.recycler.setSpacing
import fargo.appsample.core.ui.recycler.submitList
import fargo.appsample.feature.pagination.Pagination
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.core.styles.AndroidStyles
import splitties.views.dsl.recyclerview.recyclerView

class PaginationUi<T>(
    override val ctx: Context,
    private val msgConsumer: MsgConsumer<Pagination.Msg<T>>,
    private val diff: DiffUtil.ItemCallback<Any>,
    vararg delegates: AdapterDelegate<Any>
) : Ui {
    private val androidStyles = AndroidStyles(ctx)

    private val initialLoadingView =
        androidStyles.progressBar.default(R.id.loading_view)

    private val lManager = LinearLayoutManager(ctx)

    private val scrollListener =
        PaginationScrollListener(lManager) {
            msgConsumer.send(Pagination.Msg.LoadNextPage)
        }

    private val recyclerView = recyclerView(R.id.recycler_view) {
        layoutManager = lManager
        adapter = adapter(diff) {
            for (delegate in delegates) {
                this.delegates.add(delegate)
            }
            bind { PageLoadingItemUi(ctx) }
            bind { PageErrorItemUi(ctx, msgConsumer) }
        }
        addOnScrollListener(scrollListener)
        setSpacing(dip(6), dip(8), dip(4))
    }

    private val refreshLayout = view(::SwipeRefreshLayout, R.id.str_layout) {
        setOnRefreshListener { msgConsumer.send(Pagination.Msg.Refresh) }
        add(recyclerView, ViewGroup.LayoutParams(matchParent, matchParent))
    }

    private var snackbar: Snackbar? = null

    override val root = frameLayout {
        add(refreshLayout, lParams(matchParent, wrapContent))
        add(initialLoadingView, lParams(wrapContent, wrapContent, Gravity.CENTER))
    }

    fun render(state: Pagination.State<T>) {
        // Initial loading:
        initialLoadingView.isVisible = state.loading == Pagination.Loading.Initial

        // Recycler:
        val items = state.data?.items?.let {
            val additionalItems = when {
                state.loading == Pagination.Loading.NextPage ->
                    listOf<Any>(PageLoadingItem)
                state.loadingError?.first == Pagination.Loading.NextPage ->
                    listOf<Any>(state.loadingError.second)
                else -> emptyList()
            }
            it + additionalItems
        }
        recyclerView.submitList(items)

        // Scroll listener:
        scrollListener.isEnabled = state.data != null &&
                state.loading != Pagination.Loading.NextPage &&
                state.loadingError == null &&
                state.data.isEnd != true

        // Refresh:
        refreshLayout.isRefreshing = state.loading == Pagination.Loading.Refresh
        refreshLayout.isEnabled = state.data != null

        // Errors:
        if (state.loadingError?.first == Pagination.Loading.Refresh ||
            state.loadingError?.first == Pagination.Loading.Initial
        ) {
            val msg = state.loadingError.second.message ?: "No message"
            snackbar = Snackbar.make(
                root,
                msg,
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar?.setAction(R.string.action_retry) {
                if (state.loadingError.first == Pagination.Loading.Refresh) {
                    msgConsumer.send(Pagination.Msg.Refresh)
                } else {
                    msgConsumer.send(Pagination.Msg.LoadNextPage)
                }
            }
            snackbar?.setText(msg)
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }

}