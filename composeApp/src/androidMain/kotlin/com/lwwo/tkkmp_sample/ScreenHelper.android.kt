package com.lwwo.tkkmp_sample

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.Composable

// Androidで画面の高さを取得する
@Composable
actual fun getScreenHeight(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp
}

// Androidで画面の幅を取得する
@Composable
actual fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}

@Composable
actual fun getSafeAreaInsets(): Map<Any?, *> {
    return mapOf(
        "top" to 0.toDouble(),
        "bottom" to 32.toDouble(),
        "left" to 0.toDouble(),
        "right" to 0.toDouble()
    )
}

