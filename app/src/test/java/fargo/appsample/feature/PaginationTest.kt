package fargo.appsample.feature

import fargo.appsample.core.data.Try
import fargo.appsample.core.store.Update.Companion.upd
import fargo.appsample.feature.pagination.Pagination
import fargo.appsample.feature.pagination.update
import org.junit.Assert.assertEquals
import org.junit.Test

class PaginationTest {

    companion object {
        private const val pageSize = 3
        private val page0 = listOf(1, 2, 3)
        private val page1 = listOf(4, 5, 6)
        private val page2 = listOf(7, 8)

        private val page0LoadedState = Pagination.State(
            data = Pagination.PagedData(
                items = page0,
                page = 0,
                isEnd = false
            ),
            loading = null,
            loadingError = null
        )

        private val page1LoadingState =
            page0LoadedState.copy(loading = Pagination.Loading.NextPage)

        private val page1LoadedState = Pagination.State(
            data = Pagination.PagedData(
                items = page0 + page1,
                page = 1,
                isEnd = false
            ),
            loading = null,
            loadingError = null
        )

        private val page2LoadingState =
            page1LoadedState.copy(loading = Pagination.Loading.NextPage)

        private val page2LoadedState = Pagination.State(
            data = Pagination.PagedData(
                items = page0 + page1 + page2,
                page = 2,
                isEnd = true
            ),
            loading = null,
            loadingError = null
        )

        private val page2RefreshingState =
            page2LoadedState.copy(loading = Pagination.Loading.Refresh)
    }

    @Test
    fun `Pagination successfully accepts initial page`() {
        val msg = Pagination.Msg.Loaded(Try.Success(page0))
        assertEquals(
            upd(page0LoadedState),
            msg.update(Pagination.State.initialLoading(), pageSize))
    }

    @Test
    fun `Pagination successfully accepts next page`() {
        val msg = Pagination.Msg.Loaded(Try.Success(page1))
        assertEquals(
            upd(page1LoadedState),
            msg.update(page1LoadingState, pageSize))
    }

    @Test
    fun `Pagination ends on last page`() {
        val msg = Pagination.Msg.Loaded(Try.Success(page2))
        assertEquals(
            upd(page2LoadedState),
            msg.update(page2LoadingState, pageSize))
    }

    @Test
    fun `Successful pagination refresh replaces all pages to initial`() {
        val msg = Pagination.Msg.Loaded(Try.Success(page0))
        assertEquals(
            upd(page0LoadedState),
            msg.update(page2RefreshingState, pageSize)
        )
    }

    @Test
    fun `Load next page emits correct effect`() {
        val msg = Pagination.Msg.LoadNextPage
        assertEquals(
            upd(page2LoadingState, Pagination.LoadPageCmd(2, pageSize)),
            msg.update(page1LoadedState, pageSize)
        )
    }

    @Test
    fun `Load next page msg ignored while page loading`() {
        val msg = Pagination.Msg.LoadNextPage
        assertEquals(
            upd(page2LoadingState),
            msg.update(page2LoadingState, pageSize)
        )
    }

    @Test
    fun `Pagination ignores Loaded message if no loading in state`() {
        assertEquals(
            upd(page2LoadedState),
            Pagination.Msg.Loaded(Try.Success(page0)).update(page2LoadedState, pageSize)
        )
    }

    @Test
    fun `First page with few items is last`() {
        val state = Pagination.State.initialLoading<Int>()
        assertEquals(
            upd(state.copy(data = Pagination.PagedData(page2, 0, true), loading = null)),
            Pagination.Msg.Loaded(Try.Success(page2)).update(state, pageSize)
        )
    }

}