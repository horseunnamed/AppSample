package fargo.appsample.feature.breed.ui

import android.content.Context
import android.view.View
import fargo.appsample.core.store.MsgConsumer
import fargo.appsample.core.ui.recycler.item.ItemUi
import fargo.appsample.entity.DogBreed
import fargo.appsample.feature.breed.BreedSearchStore
import splitties.dimensions.dip
import splitties.views.dsl.core.*
import splitties.views.dsl.material.materialCardView
import splitties.views.onClick
import splitties.views.padding

class DogBreedUi(
    override val ctx: Context,
    private val msgConsumer: MsgConsumer<BreedSearchStore.Msg>
) : ItemUi<DogBreed> {

    private val dogNameTextView = textView {
        padding = dip(5)
        textSize = dip(10).toFloat()
    }

    override val root: View = materialCardView {
        layoutParams = lParams(matchParent, wrapContent)
        isClickable = true
        isFocusable = true
        add(dogNameTextView, lParams(matchParent, wrapContent))
    }

    override fun bind(item: DogBreed) {
        dogNameTextView.text = item.name
        root.onClick { msgConsumer.send(BreedSearchStore.Msg.SelectBreed(item)) }
    }
}