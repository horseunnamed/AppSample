package fargo.appsample.feature.pagination

import fargo.appsample.core.data.Try
import fargo.appsample.core.store.Upd
import fargo.appsample.core.store.Update

object Pagination {

    enum class Loading {
        Initial, Refresh, NextPage
    }

    data class PagedData<T>(
        val items: List<T>,
        val page: Int,
        val isEnd: Boolean
    )

    data class State<T>(
        val data: PagedData<T>?,
        val loadingError: Pair<Loading, Throwable>?,
        val loading: Loading?
    ) {
        companion object {
            fun <T> initialLoading() = State<T>(
                data = null,
                loadingError = null,
                loading = Pagination.Loading.Initial
            )
        }
    }

    sealed class Msg<out T> {
        object Reset : Msg<Nothing>()
        object Refresh : Msg<Nothing>()
        object LoadNextPage : Msg<Nothing>()
        data class Loaded<T>(val items: Try<List<T>>) : Msg<T>()
    }

    data class LoadPageCmd(val page: Int, val pageSize: Int)
}

fun <T> Pagination.Msg<T>.update(
    state: Pagination.State<T>,
    pageSize: Int
) : Upd<Pagination.State<T>, Pagination.LoadPageCmd> {
    return when (this) {
        Pagination.Msg.Reset ->
            Update.upd(
                state.copy(
                    data = null,
                    loadingError = null,
                    loading = Pagination.Loading.Initial
                ),
                Pagination.LoadPageCmd(0, pageSize)
            )
        Pagination.Msg.Refresh ->
            Update.upd(
                state.copy(
                    loadingError = null,
                    loading = Pagination.Loading.Refresh
                ),
                Pagination.LoadPageCmd(0, pageSize)
            )
        Pagination.Msg.LoadNextPage -> {
            if (state.data != null && !state.data.isEnd && state.loading == null) {
                Update.upd(
                    state.copy(loading = Pagination.Loading.NextPage),
                    Pagination.LoadPageCmd(
                        state.data.page + 1,
                        pageSize
                    )
                )
            } else {
                Update.upd(state) // ignore after the end was reached, while data is loading or if there is no data
            }
        }
        is Pagination.Msg.Loaded -> if (state.loading != null) {
            when (items) {
                is Try.Success -> {
                    val pagedData = if (state.data != null && state.loading == Pagination.Loading.NextPage) {
                        state.data.copy(
                            items = state.data.items + items.value,
                            page = state.data.page + 1,
                            isEnd = items.value.size < pageSize
                        )
                    } else {
                        Pagination.PagedData(
                            items = items.value,
                            page = 0,
                            isEnd = items.value.size < pageSize
                        )
                    }
                    Update.upd(
                        state.copy(
                            pagedData,
                            loading = null
                        )
                    )
                }
                is Try.Failure -> {
                    Update.upd(
                        state.copy(
                            loadingError = state.loading to items.error,
                            loading = null
                        )
                    )
                }
            }
        } else {
            Update.upd(state) // ignore if there is no current loading
        }
    }
}