package fargo.appsample.core.ui.recycler.item

import androidx.recyclerview.widget.RecyclerView

class UiViewHolder<T, UI : ItemUi<T>>(val ui: UI) : RecyclerView.ViewHolder(ui.root)