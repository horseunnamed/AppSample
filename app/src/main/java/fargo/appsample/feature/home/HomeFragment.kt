package fargo.appsample.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fargo.appsample.R
import fargo.appsample.core.ui.UiHolder
import fargo.appsample.feature.favorites.FavoritesFragment
import fargo.appsample.feature.search.ImgSearchFragment

class HomeFragment : Fragment(), HomeNavigation {

    private val ui by UiHolder {
        HomeUi(requireContext(), this)
    }

    override fun openSearch() {
        switchFragment(SEARCH_TAG) {
            ImgSearchFragment()
        }
    }

    override fun openFavorites() {
        switchFragment(FAVORITES_TAG) {
            FavoritesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            openSearch()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ui.root
    }

    private fun switchFragment(
        tag: String,
        fragmentCreator: () -> Fragment
    ) {
        val fragmentToDetach = childFragmentManager.findFragmentById(R.id.home_content)
        if (fragmentToDetach?.tag == tag) {
            return
        }
        val fragmentToAttach = childFragmentManager.findFragmentByTag(tag)
        childFragmentManager.beginTransaction()
            .apply {
                if (fragmentToDetach != null) {
                    detach(fragmentToDetach)
                }
                if (fragmentToAttach == null) {
                    add(R.id.home_content, fragmentCreator(), tag)
                } else {
                    attach(fragmentToAttach)
                }
            }
            .commit()
    }

    companion object {
        private const val SEARCH_TAG = "search"
        private const val FAVORITES_TAG = "favorites"
    }

}