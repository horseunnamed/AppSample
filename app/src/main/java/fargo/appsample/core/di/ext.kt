package fargo.appsample.core.di

import toothpick.Scope
import toothpick.config.Module

inline fun <reified T> Scope.get(): T = getInstance(T::class.java)

fun Scope.installModule(moduleInitializer: Module.() -> Unit) {
    installModules(object : Module() {
        init {
            moduleInitializer()
        }
    })
}
