package fargo.appsample.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fargo.appsample.R
import fargo.appsample.core.di.FragmentDI
import fargo.appsample.core.di.get
import fargo.appsample.di.module.FlowNavigationModule
import fargo.appsample.navigation.FlowRouter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen

abstract class FlowFragment : Fragment() {

    abstract fun getLauncherScreen(): SupportAppScreen

    private val navigatorHolder by lazy {
        FragmentDI.getBoundScope(this).get<NavigatorHolder>()
    }

    private val flowRouter by lazy {
        FragmentDI.getBoundScope(this).get<FlowRouter>()
    }

    private val navigator by lazy {
        SupportAppNavigator(activity, childFragmentManager, R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentDI.bindScope(this, savedInstanceState) {
            installModules(FlowNavigationModule(get()))
        }
        if (savedInstanceState == null) {
            flowRouter.newRootFlow(getLauncherScreen())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.container, container, false)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}