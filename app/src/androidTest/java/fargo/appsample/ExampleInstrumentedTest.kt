package fargo.appsample

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import fargo.appsample.di.DI
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import toothpick.Toothpick

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(AppActivity::class.java)

    private val okhttpIdlingRes by lazy {
        val client = Toothpick.openScope(DI.APP_SCOPE).getInstance(OkHttpClient::class.java)
        OkHttp3IdlingResource.create("OkHttp", client)
    }

    @Before
    fun beforeAll() {
        IdlingRegistry.getInstance().let {
            if (!it.resources.contains(okhttpIdlingRes)) {
                it.register(okhttpIdlingRes)
            }
        }
    }

    @Test
    fun showListOfDoggosAtStart() {
        onView(withId(R.id.recycler_view)).check(adapterHasCount(10))
    }

    @Test
    fun screenshotTest() {
        // onView(withId(R.id.recycler_view)).check(adapterHasCount(10))
        Espresso.onIdle()
        Thread.sleep(200)
        takeScreenshot("first_screen.png")
    }
}
