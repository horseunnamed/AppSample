package fargo.appsample.feature.home

import android.content.Context
import android.view.Gravity
import fargo.appsample.R
import splitties.views.dsl.core.*
import splitties.views.dsl.material.MaterialComponentsStyles

class HomeUi(
    override val ctx: Context,
    private val navigation: HomeNavigation
) : Ui {
    private val materialStyles = MaterialComponentsStyles(ctx)

    private val navView  = materialStyles.bottomNavigationView.default {
        inflateMenu(R.menu.home)
        setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.search_item -> navigation.openSearch()
                R.id.favorites_item -> navigation.openFavorites()
            }
            true
        }
    }

    override val root = verticalLayout {
        add(frameLayout(R.id.home_content), lParams(matchParent, matchParent, weight = 1f))
        add(navView, lParams(matchParent, wrapContent, gravity = Gravity.BOTTOM))
    }

}