package fargo.appsample.feature.pagination.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import fargo.appsample.core.ui.recycler.item.ItemUi
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.core.styles.AndroidStyles

object PageLoadingItem

class PageLoadingItemUi(override val ctx: Context) : ItemUi<PageLoadingItem> {
    private val androidStyles = AndroidStyles(ctx)

    override fun bind(item: PageLoadingItem) { }

    override val root: View = frameLayout {
        layoutParams = ViewGroup.MarginLayoutParams(matchParent, wrapContent).apply {
            verticalMargin = dip(2)
        }
        add(androidStyles.progressBar.default(),
            lParams(wrapContent, wrapContent, Gravity.CENTER))
    }
}