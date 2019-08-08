package fargo.appsample.feature.pagination.ui

import android.content.Context
import fargo.appsample.R
import fargo.appsample.core.store.MsgConsumer
import fargo.appsample.core.ui.recycler.item.ItemUi
import fargo.appsample.feature.pagination.Pagination
import splitties.views.dsl.core.*

class PageErrorItemUi<T>(
    override val ctx: Context,
    private val msgConsumer: MsgConsumer<Pagination.Msg<T>>
) : ItemUi<Throwable> {

    private val errorMessage = textView { }

    private val retryButton = button {
        text = ctx.getString(R.string.action_retry)
        setOnClickListener {
            msgConsumer.send(Pagination.Msg.LoadNextPage)
        }
    }

    override val root = verticalLayout {
        add(errorMessage, lParams(matchParent, wrapContent))
        add(retryButton, lParams(matchParent, wrapContent))
    }

    override fun bind(item: Throwable) {
        errorMessage.text = item.message
    }

}