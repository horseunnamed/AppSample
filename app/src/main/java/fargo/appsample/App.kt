package fargo.appsample

import android.app.Application
import fargo.appsample.core.di.FragmentDI
import fargo.appsample.di.DI
import fargo.appsample.di.module.AppModule
import fargo.appsample.di.module.NetworkModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initTP()
        initAppScope()
        initFragmentDI()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun initTP() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE)
            .installModules(NetworkModule(), AppModule(this))
    }

    private fun initFragmentDI() {
        FragmentDI.init(this)
    }

}