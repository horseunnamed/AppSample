package fargo.appsample.core.store

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class StoreHolder(val store: Store<*, *, *>) : ViewModel() {
    override fun onCleared() {
        store.stop()
    }
}

class RetainedStore<ST : Store<*, *, *>>(
    private val creator: () -> ST
) : ReadOnlyProperty<Fragment, ST> {

    private var value: ST? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): ST {
        @Suppress("UNCHECKED_CAST")
        return value ?: (getStoreHolder(thisRef).store as ST).also { value = it }
    }

    private fun createVMFactory(storeCreator: () -> ST) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return StoreHolder(storeCreator()) as T
        }
    }

    private fun getStoreHolder(fragment: Fragment): StoreHolder {
        return ViewModelProviders
            .of(fragment, createVMFactory(creator))
            .get(StoreHolder::class.java)
    }

}