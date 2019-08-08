package fargo.appsample.core.ui.recycler.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fargo.appsample.core.ui.recycler.item.ItemUi
import fargo.appsample.core.ui.recycler.item.UiViewHolder
import splitties.views.dsl.core.Ui

class AdapterBuilderContext(val delegates: MutableList<AdapterDelegate<Any>>) {
    inline fun <reified T, UI : ItemUi<T>> bind(crossinline uiCreator: (Context) -> UI) {
        val delegate = object : AdapterDelegate<Any> {
            override fun isForItem(item: Any): Boolean = item is T
            override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                return UiViewHolder(uiCreator(parent.context))
            }
            override fun onBindViewHolder(item: Any, viewHolder: RecyclerView.ViewHolder) {
                @Suppress("UNCHECKED_CAST")
                (viewHolder as UiViewHolder<T, UI>).ui.bind(item as T)
            }
        }
        delegates.add(delegate)
    }
}

@Suppress("unused")
fun Ui.adapter(
    diff: DiffUtil.ItemCallback<Any> = DefaultDiff(),
    block: AdapterBuilderContext.() -> Unit
): DelegationAdapter<Any> {
    val delegates = mutableListOf<AdapterDelegate<Any>>()
    val context = AdapterBuilderContext(delegates)
    context.block()
    return DelegationAdapter(delegates, diff)
}

@Suppress("unused")
inline fun <reified T> Ui.adapterDelegate(
    crossinline uiCreator: (Context) -> ItemUi<T>
): AdapterDelegate<Any> {
    return object : AdapterDelegate<Any> {
        override fun isForItem(item: Any): Boolean = item is T
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            return UiViewHolder(uiCreator(parent.context))
        }
        override fun onBindViewHolder(item: Any, viewHolder: RecyclerView.ViewHolder) {
            @Suppress("UNCHECKED_CAST")
            (viewHolder as UiViewHolder<T, ItemUi<T>>).ui.bind(item as T)
        }
    }
}
