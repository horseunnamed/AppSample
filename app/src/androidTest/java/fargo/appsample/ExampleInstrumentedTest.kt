package fargo.appsample

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

    @Before
    fun before() {
        val client = Toothpick.openScope(DI.APP_SCOPE).getInstance(OkHttpClient::class.java)
        val idlingRes = OkHttp3IdlingResource.create("OkHttp", client)
        IdlingRegistry.getInstance().register(idlingRes)
    }

    @Test
    fun showListOfDoggosAtStart() {
        onView(withId(R.id.recycler_view)).check(adapterHasCount(10))
    }
}
