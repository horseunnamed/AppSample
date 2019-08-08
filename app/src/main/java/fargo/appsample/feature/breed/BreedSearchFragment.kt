package fargo.appsample.feature.breed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fargo.appsample.core.di.get
import fargo.appsample.core.fragment.BackHandler
import fargo.appsample.core.store.RetainedStore
import fargo.appsample.core.store.bindState
import fargo.appsample.core.ui.UiHolder
import fargo.appsample.core.ui.hideKeyboard
import fargo.appsample.di.DI
import fargo.appsample.feature.breed.ui.BreedSearchUi
import ru.terrakok.cicerone.Router
import toothpick.Toothpick

class BreedSearchFragment : Fragment(), BackHandler {

    private val router by lazy { Toothpick.openScope(DI.APP_SCOPE).get<Router>() }

    private val store by RetainedStore {
        Toothpick.openScope(DI.APP_SCOPE).get<BreedSearchStore>()
    }

    private val ui by UiHolder {
        BreedSearchUi(requireContext(), store, this)
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

    override fun onResume() {
        super.onResume()
        ui.focusSearchEditText()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }

    override fun onBack() {
        router.exit()
    }

}