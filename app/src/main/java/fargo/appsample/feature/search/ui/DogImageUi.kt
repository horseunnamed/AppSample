package fargo.appsample.feature.search.ui

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import fargo.appsample.core.ui.loadImage
import fargo.appsample.core.ui.recycler.item.ItemUi
import fargo.appsample.entity.DogImage
import splitties.dimensions.dip
import splitties.views.backgroundColor
import splitties.views.dsl.core.*
import splitties.views.dsl.material.materialCardView
import splitties.views.padding

class DogImageUi(override val ctx: Context) :
    ItemUi<DogImage> {
    private val dogImageView = imageView()

    private val idTextView = textView {
        backgroundColor = Color.parseColor("#88554433")
        setTextColor(Color.WHITE)
        textSize = dip(12).toFloat()
        padding = dip(5)
    }

    override val root = materialCardView {
        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        add(dogImageView, lParams(matchParent, dip(200)))
        add(idTextView, lParams(matchParent, wrapContent, Gravity.BOTTOM))
    }

    override fun bind(item: DogImage) {
        idTextView.text = item.id
        dogImageView.loadImage(item.url)
    }
}