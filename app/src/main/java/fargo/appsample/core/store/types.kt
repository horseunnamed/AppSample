package fargo.appsample.core.store

import android.os.Parcelable
import androidx.annotation.UiThread
import kotlinx.coroutines.CoroutineScope

typealias Upd<S, C> = Pair<S, C?>

interface MsgConsumer<M> {
    fun send(msg: M)
}

interface Update<S, M, C> {
    operator fun invoke(state: S, msg: M): Upd<S, C>

    companion object {
        fun <S> upd(state: S) = state to null
        fun <S, C> upd(state: S, cmd: C?) = state to cmd
    }
}

interface Bootstrapper<S, C> {
    fun getInitialState(savedState: Parcelable?): S
    fun getInitialCmd(state: S): List<C>
    fun parcelizeState(state: S): Parcelable?
}

interface CmdHandler<M, C> {
    @UiThread
    fun handle(cmd: C, scope: CoroutineScope, output: MsgConsumer<M>)
}