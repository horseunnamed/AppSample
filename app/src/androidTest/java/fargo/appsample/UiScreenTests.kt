package fargo.appsample

import androidx.test.ext.junit.runners.AndroidJUnit4
import fargo.appsample.entity.DogImage
import fargo.appsample.feature.pagination.Pagination
import fargo.appsample.feature.search.ImgSearchStore
import fargo.appsample.feature.search.ui.ImgSearchUi
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UiScreenTests {

    @Test
    fun imgSearchUi() {
        val image = DogImage(
            id = "LOL2",
            url = "https://www.laurenceanthony.net/software/pvst/icons/PVST.png",
            breeds = emptyList()
        )
        val state = ImgSearchStore.State(
            images = Pagination.State(
                data = Pagination.PagedData(
                    items = listOf(image),
                    page = 0,
                    isEnd = true
                ),
                loading = null,
                loadingError = null
            ),
            searchConfig = ImgSearchStore.SearchConfig(
                breed = null,
                type = DogImage.Type.STATIC,
                order = DogImage.Order.RANDOM
            ),
            selectedImage = null,
            likedImages = emptySet()
        )

        launchView {
            ImgSearchUi(it, mocked(), mocked()).apply { render(state) }.root
        }

        Thread.sleep(2000)
        takeScreenshot("img_search_ui")
    }

}