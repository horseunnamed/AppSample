package fargo.appsample.di.module

import fargo.appsample.navigation.FlowRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class FlowNavigationModule(parentRouter: Router) : Module() {
    init {
        val cicerone = Cicerone.create(FlowRouter(parentRouter))
        bind(FlowRouter::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}