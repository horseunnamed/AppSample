package fargo.appsample.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fargo.appsample.navigation.NavDest
import fargo.appsample.core.store.RetainedStore
import fargo.appsample.core.store.bindState
import fargo.appsample.core.di.get
import fargo.appsample.core.ui.UiHolder
import fargo.appsample.di.DI
import fargo.appsample.feature.search.ui.ImgSearchUi
import ru.terrakok.cicerone.Router
import toothpick.Toothpick

class ImgSearchFragment : Fragment(), ImgSearchNavigation {

    private val store by RetainedStore {
        Toothpick.openScope(DI.APP_SCOPE).get<ImgSearchStore>()
    }

    private val ui by UiHolder {
        ImgSearchUi(requireContext(), store, this)
    }

    private val router by lazy {
        Toothpick.openScope(DI.APP_SCOPE).get<Router>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store.start(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindState(store, ui::render)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        store.saveStateTo(outState)
    }

    override fun openBreedSelection() {
        router.navigateTo(NavDest.BreedSearchScreen())
    }
}
