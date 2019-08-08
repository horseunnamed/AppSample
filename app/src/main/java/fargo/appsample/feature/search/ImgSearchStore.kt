package fargo.appsample.feature.search

import android.os.Parcelable
import fargo.appsample.core.store.Update.Companion.upd
import fargo.appsample.core.async.performIO
import fargo.appsample.core.data.toTry
import fargo.appsample.core.store.*
import fargo.appsample.entity.DogBreed
import fargo.appsample.entity.DogImage
import fargo.appsample.feature.DogBreedSelection
import fargo.appsample.feature.pagination.Pagination
import fargo.appsample.feature.pagination.update
import fargo.appsample.network.DogsApi
import fargo.appsample.network.toMimeTypes
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImgSearchStore @Inject constructor(
    effects: Effects
) : Store<ImgSearchStore.State, ImgSearchStore.Msg, ImgSearchStore.Cmd>(
    BootstrapperImpl(), UpdateImpl, effects
) {

    @Parcelize
    data class SearchConfig(
        val breed: DogBreed? = null,
        val type: DogImage.Type = DogImage.Type.ALL,
        val order: DogImage.Order = DogImage.Order.ASC
    ) : Parcelable

    data class State(
        val images: Pagination.State<DogImage>,
        val searchConfig: SearchConfig,
        val selectedImage: String?,
        val likedImages: Set<String>
    ) {
        companion object {
            fun initialLoading() = State(
                images = Pagination.State.initialLoading(),
                searchConfig = SearchConfig(),
                selectedImage = null,
                likedImages = emptySet()
            )
        }
    }

    sealed class Msg {
        sealed class SearchInput : Msg() {
            data class Type(val value: DogImage.Type) : SearchInput()
            data class Order(val value: DogImage.Order) : SearchInput()
            data class Breed(val value: DogBreed?) : SearchInput()
        }
        data class Pag(val value: Pagination.Msg<DogImage>) : Msg()
        data class SelectImage(val imageId: String) : Msg()
        data class LikeImage(val imageId: String) : Msg()
        data class DislikeImage(val imageId: String) : Msg()
    }

    sealed class Cmd {
        object SubscribeBreedSelections : Cmd()
        data class LoadImages(
            val searchConfig: SearchConfig,
            val pagCmd: Pagination.LoadPageCmd
        ) : Cmd()
    }

    object UpdateImpl : Update<State, Msg, Cmd> {
        override fun invoke(state: State, msg: Msg): Upd<State, Cmd> =
            when (msg) {
                is Msg.SearchInput -> msg.update(state)
                is Msg.Pag -> {
                    val (pagState, pagCmd) = msg.value.update(state.images, pageSize)
                    upd(state.copy(images = pagState),
                        pagCmd?.let { Cmd.LoadImages(state.searchConfig, pagCmd) })
                }
                is Msg.SelectImage -> upd(state.copy(selectedImage = msg.imageId))
                is Msg.LikeImage -> upd(state.copy(likedImages = state.likedImages + msg.imageId))
                is Msg.DislikeImage -> upd(state.copy(likedImages = state.likedImages - msg.imageId))
            }

        private fun Msg.SearchInput.update(state: State): Upd<State, Cmd> {
            val newSearchConfig = when (this) {
                is Msg.SearchInput.Type -> state.searchConfig.copy(type = value)
                is Msg.SearchInput.Order -> state.searchConfig.copy(order = value)
                is Msg.SearchInput.Breed -> state.searchConfig.copy(breed = value)
            }

            return upd(
                state.copy(
                    images = Pagination.State.initialLoading(),
                    searchConfig = newSearchConfig),
                Cmd.LoadImages(newSearchConfig, Pagination.LoadPageCmd(0, pageSize)))
        }

    }

    class Effects @Inject constructor(
        private val dogsApi: DogsApi,
        private val breedSelections: DogBreedSelection
    ) : CmdHandler<Msg, Cmd> {
        private var searchJob: Job? = null

        override fun handle(cmd: Cmd, scope: CoroutineScope, output: MsgConsumer<Msg>) {
            when (cmd) {
                is Cmd.SubscribeBreedSelections -> scope.launch {
                    breedSelections.subscribe { output.send(Msg.SearchInput.Breed(it)) }
                }
                is Cmd.LoadImages -> {
                    searchJob?.cancel()
                    searchJob = scope.performIO { output.send(Msg.Pag(cmd.handle())) }
                }
            }
        }

        private suspend fun Cmd.LoadImages.handle(): Pagination.Msg.Loaded<DogImage> =
            toTry {
                dogsApi.getImages(
                    breedId = searchConfig.breed?.id,
                    mimeTypes = searchConfig.type.toMimeTypes(),
                    order = searchConfig.order,
                    page = pagCmd.page,
                    limit = pagCmd.pageSize
                )
            }.let { Pagination.Msg.Loaded(it) }
    }

    class BootstrapperImpl : Bootstrapper<State, Cmd> {
        override fun getInitialState(savedState: Parcelable?): State {
            val restoredSearchConfig = savedState as? SearchConfig
            val restoredState = restoredSearchConfig?.let {
                State.initialLoading().copy(searchConfig = restoredSearchConfig)
            }
            return restoredState ?: State.initialLoading()
        }

        override fun parcelizeState(state: State): Parcelable? {
            return state.searchConfig
        }

        override fun getInitialCmd(state: State): List<Cmd> {
            return listOf(
                Cmd.SubscribeBreedSelections,
                Cmd.LoadImages(state.searchConfig, Pagination.LoadPageCmd(0, pageSize)))
        }
    }

    companion object {
        const val pageSize = 10
    }

}