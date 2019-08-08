@file:Suppress("unused")

package fargo.appsample.core.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import fargo.appsample.R
import fargo.appsample.core.fragment.BackHandler
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.core.Ui
import splitties.views.dsl.core.wrapContent

fun Ui.regularToolbar(title: String, backHandler: BackHandler? = null) = toolbar(R.id.toolbar) {
    setBackgroundResource(R.color.colorPrimary)
    setTitleTextColor(Color.WHITE)
    if (backHandler != null) {
        setContentInsetsRelative(0 ,0)
        setContentInsetsAbsolute(0, 0)
        setNavigationIcon(R.drawable.ic_arrow)
        setNavigationOnClickListener { backHandler.onBack() }
    }
    popupTheme = R.style.ThemeOverlay_AppCompat_Light
    this.title = title
}

inline fun AppBarLayout.appBarLParams(
    width: Int = wrapContent,
    height: Int = wrapContent,
    initParams: AppBarLayout.LayoutParams.() -> Unit = {}
): AppBarLayout.LayoutParams {
    return AppBarLayout.LayoutParams(width, height).apply(initParams)
}

fun Ui.textWatcher(textChangedListener: (String) -> Unit): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            textChangedListener(p0.toString())
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
    }
}

fun Fragment.hideKeyboard() {
    val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity?.currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this).load(url).into(this)
}

fun EditText.updateText(watcher: TextWatcher, newText: String) {
    removeTextChangedListener(watcher)
    val editable = text
    if (!newText.contentEquals(editable)) {
        if (editable is Spanned) {
            val ss = SpannableString(newText)
            TextUtils.copySpansFrom(editable, 0, ss.length, null, ss, 0)
            editable.replace(0, editable.length, ss)
        } else {
            editable.replace(0, editable.length, newText)
        }
    }
    addTextChangedListener(watcher)
}

fun EditText.focusAndShowKeyboard() {
    requestFocus()
    val imm = (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}



