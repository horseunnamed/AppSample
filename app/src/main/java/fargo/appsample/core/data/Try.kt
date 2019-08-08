package fargo.appsample.core.data

import kotlinx.coroutines.CancellationException
import timber.log.Timber

sealed class Try<out T> {

    data class Success<T>(val value: T) : Try<T>()
    data class Failure(val error: Throwable) : Try<Nothing>()

    inline fun <R> map(transform: (T) -> R): Try<R> = when (this) {
        is Success -> Success(
            transform(
                value
            )
        )
        is Failure -> this
    }

}

suspend fun <T> toTry(block: suspend () -> T): Try<T> =
    try {
        Try.Success(block())
    } catch (e: Throwable) {
        if (e is CancellationException) {
            throw e
        }
        Timber.e(e)
        Try.Failure(e)
    }
