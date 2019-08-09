package fargo.appsample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import java.io.File
import java.io.IOException

class MyScreenCaptureProcessor : BasicScreenCaptureProcessor() {
    private val deviceScreenshotsDir = "/sdcard/Pictures/appsample/"

    init {
        this.mDefaultScreenshotPath = File(deviceScreenshotsDir)
    }

    override fun getFilename(prefix: String): String = prefix
}

fun takeScreenshot(screenShotName: String) {
    Log.d("Screenshots", "Taking screenshot of '$screenShotName'")
    val screenCapture = Screenshot.capture()
    val processors = setOf(MyScreenCaptureProcessor())
    try {
        screenCapture.apply {
            name = screenShotName
            process(processors)
        }
        Log.d("Screenshots", "Screenshot taken")
    } catch (ex: IOException) {
        Log.e("Screenshots", "Could not take the screenshot", ex)
    }
}

class ViewHostFragment(val initView: (Context) -> View) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(requireContext())
    }
}

fun launchView(initView: (Context) -> View) {
    launchFragmentInContainer(themeResId = R.style.AppTheme) {
        ViewHostFragment(initView)
    }
}