package fargo.appsample.core.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import fargo.appsample.di.DI
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick

abstract class DIFragment : Fragment() {
    private lateinit var fragmentDIScopeName: String

    private val parentDIScopeName: String by lazy {
        (parentFragment as? DIFragment)?.fragmentDIScopeName ?: DI.APP_SCOPE
    }

    private var instanceStateSaved: Boolean = false

    protected lateinit var diScope: Scope
        private set

    protected open fun onOpenDIScope(scope: Scope) { }
    protected open fun onCloseDIScope() { }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentDIScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?:
                "${javaClass.simpleName}_${hashCode()}"

        if (Toothpick.isScopeOpen(fragmentDIScopeName)) {
            Timber.d("Get exist UI scope: $fragmentDIScopeName")
            diScope = Toothpick.openScope(fragmentDIScopeName)
        } else {
            Timber.d("Init new UI scope: $fragmentDIScopeName")
            diScope = Toothpick.openScopes(parentDIScopeName, fragmentDIScopeName)
            onOpenDIScope(diScope)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentDIScopeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            Timber.d("Destroy UI scope: $fragmentDIScopeName")
            onCloseDIScope()
            Toothpick.closeScope(diScope.name)
        }
    }

    private fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved) ||
                ((parentFragment as? DIFragment)?.isRealRemoving() ?: false)

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    companion object {
        private const val STATE_SCOPE_NAME = "state_scope_name"
    }

}