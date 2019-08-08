package fargo.appsample.di.module

import android.content.Context
import fargo.appsample.feature.DogBreedSelection
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule(appContext: Context) : Module() {
    init {
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
        bind(DogBreedSelection::class.java).toInstance(DogBreedSelection())
        bind(Context::class.java).toInstance(appContext)
    }
}