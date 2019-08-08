package fargo.appsample.feature.search.ui

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import fargo.appsample.R
import fargo.appsample.core.store.MsgConsumer
import fargo.appsample.core.store.wrap
import fargo.appsample.core.ui.appBarLParams
import fargo.appsample.core.ui.recycler.adapter.adapterDelegate
import fargo.appsample.core.ui.regularToolbar
import fargo.appsample.entity.DogImage
import fargo.appsample.feature.pagination.ui.PaginationUi
import fargo.appsample.feature.search.DogImageDiff
import fargo.appsample.feature.search.ImgSearchNavigation
import fargo.appsample.feature.search.ImgSearchStore
import splitties.dimensions.dip
import splitties.views.dsl.coordinatorlayout.appBarLParams
import splitties.views.dsl.coordinatorlayout.coordinatorLayout
import splitties.views.dsl.core.*
import splitties.views.dsl.material.MaterialComponentsStyles
import splitties.views.dsl.material.appBarLayout
import splitties.views.dsl.material.contentScrollingWithAppBarLParams
import splitties.views.dsl.material.materialCardView
import splitties.views.padding
import kotlin.reflect.KClass

class ImgSearchUi(
    override val ctx: Context,
    private val msgConsumer: MsgConsumer<ImgSearchStore.Msg>,
    private val navigation: ImgSearchNavigation
) : Ui {
    private val materialStyles = MaterialComponentsStyles(ctx)

    private val typeDropdown = createOutlineDropdown(
        R.string.img_search_type,
        DogImage.Type::class
    ) {
        msgConsumer.send(ImgSearchStore.Msg.SearchInput.Type(DogImage.Type.values()[it]))
    }

    private val orderDropdown = createOutlineDropdown(
        R.string.img_search_order,
        DogImage.Order::class
    ) {
        msgConsumer.send(ImgSearchStore.Msg.SearchInput.Order(DogImage.Order.values()[it]))
    }

    private val selectBreedButton = button {
        setOnClickListener { navigation.openBreedSelection() }
        if (isInEditMode) {
            text = ctx.getString(R.string.img_search_select_breed)
        }
    }

    private val resetBreedButton = button {
        setOnClickListener { msgConsumer.send(ImgSearchStore.Msg.SearchInput.Breed(null)) }
        text = ctx.getString(R.string.img_search_reset_breed)
    }

    private val paginationUi = PaginationUi(
        ctx,
        msgConsumer.wrap(ImgSearchStore.Msg::Pag),
        DogImageDiff(),
        adapterDelegate(::DogImageUi)
    )

    private val searchCardView = materialCardView {
        add(verticalLayout {
            padding = dip(8)
            add(horizontalLayout {
                add(orderDropdown, lParams(matchParent, wrapContent, weight = 1f) {
                    endMargin = dip(4)
                })
                add(typeDropdown, lParams(matchParent, wrapContent, weight = 1f) {
                    startMargin = dip(4)
                })
            }, lParams(matchParent, wrapContent))
            add(selectBreedButton, lParams(matchParent, wrapContent) {
                topMargin = dip(8)
            })
            add(resetBreedButton, lParams(matchParent, wrapContent))
        }, lParams(matchParent, wrapContent))
    }

    private val appBar = appBarLayout(R.id.app_bar_layout) {
        add(regularToolbar(ctx.getString(R.string.app_name)), appBarLParams(matchParent, wrapContent) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        })
        add(searchCardView, appBarLParams(matchParent, wrapContent) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            setMargins(dip(8), 0, dip(8), dip(8))
        })
    }

    override val root: View = coordinatorLayout(R.id.coordinator) {
        add(appBar, appBarLParams())
        add(paginationUi.root, contentScrollingWithAppBarLParams())
    }

    fun render(state: ImgSearchStore.State) {
        // Dropdowns:
        orderDropdown.setDropdownSelection(DogImage.Order.values().indexOf(state.searchConfig.order))
        typeDropdown.setDropdownSelection(DogImage.Type.values().indexOf(state.searchConfig.type))

        // Buttons:
        if (state.searchConfig.breed == null) {
            selectBreedButton.text = ctx.getText(R.string.img_search_select_breed)
            resetBreedButton.isVisible = false
        } else {
            selectBreedButton.text = state.searchConfig.breed.name
            resetBreedButton.isVisible = true
        }

        // Pagination:
        paginationUi.render(state.images)
    }

    private fun TextInputLayout.setDropdownSelection(position: Int) {
        (editText as AutoCompleteTextView).apply {
            setText(adapter.getItem(position).toString(), false)
        }
    }

    private fun createOutlineDropdown(
        @StringRes hintRes: Int,
        enumClass: KClass<out Enum<*>>,
        onSelected: (Int) -> Unit
    ): TextInputLayout {
        return materialStyles.textInputLayout.outlinedBox {
            if (!isInEditMode) {
                endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
            }
            hint = ctx.getString(hintRes)
            isHintAnimationEnabled = false
            add(autoCompleteTextView {
                setPadding(dip(4), 0, dip(4), 0)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                keyListener = null
                setAdapter(ArrayAdapter(
                    ctx,
                    R.layout.dropdown_menu_popup_item,
                    enumClass.java.enumConstants!!.map { it.name }
                ))
                onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
                    onSelected(i)
                }
            }, lParams(matchParent, wrapContent))
        }
    }

}

