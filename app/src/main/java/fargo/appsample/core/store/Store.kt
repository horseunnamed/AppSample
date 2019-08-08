package fargo.appsample.core.store

import android.os.Bundle
import androidx.annotation.UiThread
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber

open class Store<S, M, C>(
    private val bootstrapper: Bootstrapper<S, C>,
    private val update: Update<S, M, C>,
    private val cmdHandler: CmdHandler<M, C>
) : MsgConsumer<M>, CoroutineScope {

    private val logTag = javaClass.simpleName
    private var isStarted = false
    private var stateChannel =
        Channel<S>(Channel.CONFLATED)
    private val msgChannel =
        Channel<M>(Channel.UNLIMITED)
    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    var state: S? = null
        private set

    val stateReceiveChannel = stateChannel as ReceiveChannel<S>

    @UiThread
    fun start(savedState: Bundle?) {
        if (!isStarted) {
            val initState = bootstrapper.getInitialState(savedState?.getParcelable(STATE_KEY))

            updateState(initState)
            bootstrapper
                .getInitialCmd(initState)
                .forEach { it.execute() }

            launch {
                for (action in msgChannel) {
                    state?.let {
                        log("[Msg]: ${action.toString()}")
                        val (newState, cmd) = update(it, action)
                        updateState(newState)
                        cmd?.execute()
                    }
                }
            }
            isStarted = true
        }
    }

    fun saveStateTo(outState: Bundle) {
        state?.let {
            outState.putParcelable(STATE_KEY, bootstrapper.parcelizeState(it))
        }
    }

    fun stop() {
        coroutineContext.cancel()
        isStarted = false
    }

    override fun send(msg: M) {
        assert(msgChannel.offer(msg))
    }

    private fun updateState(newState: S) {
        log("[State]: ${toString()}")
        state = newState
        assert(stateChannel.offer(newState))
    }

    private fun C.execute() {
        log("[Cmd]: ${toString()}")
        cmdHandler.handle(this, this@Store, this@Store)
    }

    private fun log(message: String) {
        Timber.tag(logTag)
        Timber.d(message)
    }

    companion object {
        private const val STATE_KEY = "parcelable_state_key"
    }
}