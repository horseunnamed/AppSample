package fargo.appsample.feature.breed

import android.os.Parcelable
import fargo.appsample.core.store.Update.Companion.upd
import fargo.appsample.core.async.performIO
import fargo.appsample.core.data.Try
import fargo.appsample.core.data.toTry
import fargo.appsample.core.store.*
import fargo.appsample.entity.DogBreed
import fargo.appsample.feature.DogBreedSelection
import fargo.appsample.network.DogsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class BreedSearchStore @Inject constructor(
    effects: Effects
) : Store<BreedSearchStore.State, BreedSearchStore.Msg, BreedSearchStore.Cmd>(
    BootstrapperImpl(), UpdateImpl(), effects
) {

    data class State(
        val searchQuery: String = "",
        val inProgress: Boolean = false,
        val error: Throwable? = null,
        val breeds: List<DogBreed>? = null
    )

    sealed class Msg {
        data class SearchInput(val query: String) : Msg()
        data class SelectBreed(val breed: DogBreed) : Msg()
        data class BreedSuggestionsResult(val result: Try<List<DogBreed>>) : Msg()
    }

    sealed class Cmd {
        data class FetchBreedSuggestions(val query: String) : Cmd()
        object CancelBreedSuggestionsFetch : Cmd()
        data class DispatchBreedSelection(val breed: DogBreed) : Cmd()
    }

    class BootstrapperImpl : Bootstrapper<State, Cmd> {
        override fun getInitialState(savedState: Parcelable?): State {
            return State()
        }

        override fun getInitialCmd(state: State): List<Cmd> {
            return emptyList()
        }

        override fun parcelizeState(state: State): Parcelable? {
            return null
        }
    }

    class UpdateImpl : Update<State, Msg, Cmd> {
        override fun invoke(state: State, msg: Msg): Upd<State, Cmd> = when (msg) {
            is Msg.SearchInput -> msg.update(state)
            is Msg.SelectBreed -> upd(state, Cmd.DispatchBreedSelection(msg.breed))
            is Msg.BreedSuggestionsResult -> msg.update(state)
        }

        private fun Msg.SearchInput.update(state: State): Upd<State, Cmd> =
            if (query.length > 2) {
                upd(state.copy(searchQuery = query, inProgress = true),
                    Cmd.FetchBreedSuggestions(query))
            } else {
                upd(state.copy(searchQuery = query, inProgress = false, breeds = null),
                    Cmd.CancelBreedSuggestionsFetch)
            }

        private fun Msg.BreedSuggestionsResult.update(state: State): Upd<State, Cmd> =
            when (result) {
                is Try.Failure -> upd(state.copy(inProgress = false, error = result.error))
                is Try.Success -> upd(state.copy(inProgress = false, breeds = result.value))
            }

    }

    class Effects @Inject constructor(
        private val dogsApi: DogsApi,
        private val router: Router,
        private val breedSelections: DogBreedSelection
    ) : CmdHandler<Msg, Cmd> {
        private var job: Job? = null

        override fun handle(cmd: Cmd, scope: CoroutineScope, output: MsgConsumer<Msg>) {
            when (cmd) {
                is Cmd.FetchBreedSuggestions -> {
                    job?.cancel()
                    job = scope.performIO {
                        delay(300)
                        output.send(cmd.handle())
                    }
                }
                is Cmd.CancelBreedSuggestionsFetch -> {
                    job?.cancel()
                }
                is Cmd.DispatchBreedSelection -> {
                    breedSelections.send(cmd.breed)
                    router.exit()
                }
            }
        }

        private suspend fun Cmd.FetchBreedSuggestions.handle(): Msg.BreedSuggestionsResult {
            return Msg.BreedSuggestionsResult(toTry {
                dogsApi.searchBreeds(
                    query
                )
            })
        }
    }
}