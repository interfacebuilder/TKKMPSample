package com.lwwo.tkkmp_sample

import androidx.compose.ui.window.ComposeUIViewController
import androidx.compose.runtime.remember

fun MainViewController() = ComposeUIViewController {
    val gyroManager = remember {
        DeviceGyroManageriOS()
    }
    val screenRotator = remember {
        ScreenRotatorIOS()
    }
    MainComposable(gyroManager, screenRotator)
}