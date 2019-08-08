package fargo.appsample

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.`is`

fun adapterHasCount(count: Int): ViewAssertion =
    ViewAssertion { view, _ ->
        val recyclerView = view as RecyclerView
        assertThat(recyclerView.adapter!!.itemCount, `is`(count))
    }

