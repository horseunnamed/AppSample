package fargo.appsample.navigation

import androidx.fragment.app.Fragment
import fargo.appsample.feature.breed.BreedSearchFragment
import fargo.appsample.feature.home.HomeFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object NavDest {

    class HomeScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return HomeFragment()
        }
    }

    class BreedSearchScreen : SupportAppScreen() {
        override fun getFragment(): Fragment {
            return BreedSearchFragment()
        }
    }

}