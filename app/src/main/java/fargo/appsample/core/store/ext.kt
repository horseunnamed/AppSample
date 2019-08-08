package fargo.appsample.core.store

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

fun <S> Fragment.bindState(store: Store<S, *, *>, consumer: (S) -> Unit) {
    store.state?.let(consumer)
    viewLifecycleOwner.lifecycleScope.launch {
        for (state in store.stateReceiveChannel) {
            consumer(state)
        }
    }
}

fun <M1, M2> MsgConsumer<M1>.wrap(converter: (M2) -> M1): MsgConsumer<M2> {
    val sourceConsumer = this
    return object : MsgConsumer<M2> {
        override fun send(msg: M2) {
            sourceConsumer.send(converter(msg))
        }
    }
}
