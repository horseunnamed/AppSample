package fargo.appsample.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class FlowRouter(private val parentRouter: Router) : Router() {

    fun startFlow(screen: SupportAppScreen) {
        parentRouter.navigateTo(screen)
    }

    fun newRootFlow(screen: SupportAppScreen) {
        parentRouter.newRootScreen(screen)
    }

    fun finishFlow() {
        parentRouter.exit()
    }

}