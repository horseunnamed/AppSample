package fargo.appsample

import android.util.Log
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

