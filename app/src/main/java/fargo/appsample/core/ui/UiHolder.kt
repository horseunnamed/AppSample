package fargo.appsample.core.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class UiHolder<UI>(
    private val uiCreator: () -> UI
) : ReadOnlyProperty<Fragment, UI> {

    private var ui: UI? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): UI {
        return ui ?: createUi(thisRef.viewLifecycleOwner)
    }

    private fun createUi(viewLifecycleOwner: LifecycleOwner): UI {
        val result = uiCreator()
        viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed() {
                ui = null
            }
        })
        ui = result
        return result
    }

}

