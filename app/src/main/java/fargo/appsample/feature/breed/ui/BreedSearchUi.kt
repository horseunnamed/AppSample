package fargo.appsample.feature.breed.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fargo.appsample.R
import fargo.appsample.core.fragment.BackHandler
import fargo.appsample.core.store.MsgConsumer
import fargo.appsample.core.ui.focusAndShowKeyboard
import fargo.appsample.core.ui.recycler.adapter.adapter
import fargo.appsample.core.ui.recycler.submitList
import fargo.appsample.core.ui.regularToolbar
import fargo.appsample.core.ui.textWatcher
import fargo.appsample.core.ui.updateText
import fargo.appsample.feature.breed.BreedSearchStore
import fargo.appsample.feature.breed.DogBreedDiff
import splitties.views.dsl.core.*
import splitties.views.dsl.core.styles.AndroidStyles
import splitties.views.dsl.recyclerview.recyclerView

class BreedSearchUi(
    override val ctx: Context,
    private val msgConsumer: MsgConsumer<BreedSearchStore.Msg>,
    private val backHandler: BackHandler
) : Ui {

    private val androidStyles = AndroidStyles(ctx)

    private val searchTextWatcher = textWatcher {
        msgConsumer.send(BreedSearchStore.Msg.SearchInput(it))
    }

    private val searchEditText = editText {
        hint = ctx.getString(R.string.breed_search_hint)
        addTextChangedListener(searchTextWatcher)
    }

    private val breedsRecyclerView = recyclerView {
        id = R.id.recycler_view
        layoutManager = LinearLayoutManager(ctx)
        adapter = adapter(DogBreedDiff()) {
            bind { DogBreedUi(ctx, msgConsumer) }
        }
    }

    private val noResultsTextView = textView {
        text = ctx.getString(R.string.breed_search_empty)
    }

    private val progressView = androidStyles.progressBar.small()

    override val root: View
        get() = verticalLayout {
            add(regularToolbar(ctx.getString(R.string.breed_search_title), backHandler),
                lParams(matchParent, wrapContent))
            add(searchEditText, lParams(matchParent, wrapContent))
            add(frameLayout {
                add(noResultsTextView, lParams(wrapContent, wrapContent, Gravity.CENTER))
                add(breedsRecyclerView, lParams(matchParent, matchParent))
                add(progressView, lParams(wrapContent, wrapContent, Gravity.CENTER))
            }, lParams(matchParent, matchParent))
        }

    fun focusSearchEditText() {
        searchEditText.focusAndShowKeyboard()
    }

    fun render(state: BreedSearchStore.State) {
        searchEditText.updateText(searchTextWatcher, state.searchQuery)
        breedsRecyclerView.submitList(state.breeds)
        val isEmptyResult =
            state.searchQuery.isNotEmpty() && state.breeds?.isEmpty() == true && !state.inProgress
        breedsRecyclerView.isVisible = !isEmptyResult
        noResultsTextView.isVisible = isEmptyResult
        progressView.isVisible = state.inProgress
    }
}