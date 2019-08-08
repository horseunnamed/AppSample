package fargo.appsample.core.async

import kotlinx.coroutines.*

fun CoroutineScope.performIO(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(Dispatchers.IO, CoroutineStart.DEFAULT, block)
}

