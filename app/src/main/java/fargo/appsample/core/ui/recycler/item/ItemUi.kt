package fargo.appsample.core.ui.recycler.item

import splitties.views.dsl.core.Ui

interface ItemUi<T> : Ui {
    fun bind(item: T)
}