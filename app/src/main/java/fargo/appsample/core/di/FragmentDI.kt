package fargo.appsample.core.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import fargo.appsample.di.DI
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick

object FragmentDI : FragmentManager.FragmentLifecycleCallbacks() {
    private const val STATE_SCOPE_NAME = "state_scope_name"
    private val scopeNames = mutableMapOf<Fragment, String>()
    private var isInSaveState = false

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {}
            override fun onActivityStarted(p0: Activity) {}
            override fun onActivityDestroyed(p0: Activity) {}
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            override fun onActivityStopped(p0: Activity) {}
            override fun onActivityResumed(p0: Activity) {}
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                if (activity is AppCompatActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        this@FragmentDI, true)
                }
            }
        })
    }

    fun bindScope(fragment: Fragment, savedState: Bundle?, initScope: Scope.() -> Unit) {
        val fragmentScopeName = savedState?.getString(STATE_SCOPE_NAME)
            ?: generateScopeName(fragment)
        scopeNames[fragment] = fragmentScopeName
        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            Timber.d("Get existing DI scope: $fragmentScopeName")
        } else {
            Timber.d("Init new DI scope: $fragmentScopeName")
            val scope = Toothpick.openScopes(getParentScopeName(fragment), fragmentScopeName)
            scope.initScope()
        }
    }

    fun getBoundScope(fragment: Fragment): Scope {
        if (!scopeNames.containsKey(fragment)) {
            throw Exception("DI scope for ${fragment.javaClass.simpleName} was not bound")
        }
        return Toothpick.openScope(scopeNames[fragment])
    }

    private fun getParentScopeName(fragment: Fragment): String {
        val parent = fragment.parentFragment
        return if (parent != null) {
            scopeNames[parent] ?: DI.APP_SCOPE
        } else {
            DI.APP_SCOPE
        }
    }

    private fun generateScopeName(fragment: Fragment): String {
        return "${fragment.javaClass.simpleName}_${fragment.hashCode()}"
    }

    // region Fragment lifecycle callbacks

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        isInSaveState = false
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        isInSaveState = false
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        isInSaveState = true
        scopeNames[f]?.let {
            outState.putString(STATE_SCOPE_NAME, it)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        scopeNames[f]?.let { scopeName ->
            val activityFinishing = f.requireActivity().isFinishing

            var anyParentIsRemoving = false
            var parent = f.parentFragment
            while (!anyParentIsRemoving && parent != null) {
                anyParentIsRemoving = parent.isRemoving
                parent = parent.parentFragment
            }

            val realDestroy = !isInSaveState &&
                    (f.isRemoving || anyParentIsRemoving || activityFinishing)

            if (realDestroy) {
                if (f is DIScopeCallback) {
                    f.onCloseDIScope()
                }
                Toothpick.closeScope(scopeName)
            }
        }
        isInSaveState = false
    }

    // endregion

}