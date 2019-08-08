package fargo.appsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fargo.appsample.core.di.get
import fargo.appsample.di.DI
import fargo.appsample.navigation.NavDest
import io.palaima.debugdrawer.DebugDrawer
import io.palaima.debugdrawer.commons.BuildModule
import io.palaima.debugdrawer.commons.DeviceModule
import io.palaima.debugdrawer.commons.SettingsModule
import io.palaima.debugdrawer.network.quality.NetworkQualityModule
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.Scope
import toothpick.Toothpick

class AppActivity : AppCompatActivity() {

    private val appDIScope: Scope
        get() = Toothpick.openScope(DI.APP_SCOPE)

    private val navigatorHolder by lazy { appDIScope.get<NavigatorHolder>() }
    private val router by lazy { appDIScope.get<Router>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.container)
        initDebugDrawer()
        if (savedInstanceState == null) {
            router.replaceScreen(NavDest.HomeScreen())
        }
    }

    private fun initDebugDrawer() {
        if (BuildConfig.DEBUG) {
            DebugDrawer.Builder(this)
                .modules(
                    BuildModule(),
                    NetworkQualityModule(this),
                    DeviceModule(),
                    SettingsModule())
                .build()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(SupportAppNavigator(this, R.id.container))
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}
