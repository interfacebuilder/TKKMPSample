package com.lwwo.tkkmp_sample

import android.app.Activity
import android.content.pm.ActivityInfo
import com.lwwo.tkkmp_sample.data.ScreenRotationType

class ScreenRotatorAndroid(private val activity: Activity) : ScreenRotator {
    override fun rotateScreen(rotationType: ScreenRotationType) {
        activity.requestedOrientation =
            if (rotationType == ScreenRotationType.landscape)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
