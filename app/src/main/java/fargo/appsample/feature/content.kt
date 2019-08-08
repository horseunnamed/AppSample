package fargo.appsample.feature

import fargo.appsample.core.data.Try
import fargo.appsample.core.store.Upd
import fargo.appsample.core.store.Update.Companion.upd

object Content {
    sealed class State<out T> {
        object Empty : State<Nothing>()
        object EmptyProgress : State<Nothing>()
        data class EmptyError(val error: Throwable) : State<Nothing>()
        data class Data<T>(val value: T) : State<T>()
        data class DataRefresh<T>(val value: T) : State<T>()
        data class DataError<T>(val value: T, val refreshError: Throwable) : State<T>()
    }

    sealed class Msg<out T> {
        object Reset : Msg<Nothing>()
        object Refresh : Msg<Nothing>()
        data class Loaded<T>(val result: Try<T>): Msg<T>()
    }

    object LoadCmd
}

fun <T> Content.Msg<T>.update(state: Content.State<T>): Upd<Content.State<T>, Content.LoadCmd> {
    return when (this) {
        Content.Msg.Reset -> {
            upd(Content.State.EmptyProgress, Content.LoadCmd)
        }
        Content.Msg.Refresh -> {
            when (state) {
                is Content.State.Data<T> -> {
                    upd(Content.State.DataRefresh(state.value), Content.LoadCmd)
                }
                is Content.State.DataError<T> -> {
                    upd(Content.State.DataRefresh(state.value), Content.LoadCmd)
                }
                else -> {
                    upd(Content.State.EmptyProgress, Content.LoadCmd)
                }
            }
        }
        is Content.Msg.Loaded -> {
            when (result) {
                is Try.Success -> upd(Content.State.Data(result.value))
                is Try.Failure -> {
                    when (state) {
                        is Content.State.DataRefresh -> {
                            upd(Content.State.DataError(state.value, result.error))
                        }
                        else -> {
                            upd(Content.State.EmptyError(result.error))
                        }
                    }
                }
            }
        }
    }
}
