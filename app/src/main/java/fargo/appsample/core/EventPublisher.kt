package fargo.appsample.core

import kotlinx.coroutines.channels.Channel

abstract class EventPublisher<E> {
    private val channel = Channel<E>(Channel.UNLIMITED)

    fun send(event: E) {
        channel.offer(event)
    }

    suspend fun subscribe(onReceive: (E) -> Unit) {
        for (event in channel) {
            onReceive(event)
        }
    }
}