package com.lwwo.tkkmp_sample

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Int {
// 以下実装が取得できず0を返すので直接Swift側のUtilsクラスから取得
//    val screenSize: CGSize = UIScreen.mainScreen.bounds.useContents { size }
//    val screenHeight = screenSize.height
//    return screenHeight.toInt()
    return TKKMPSample.ScreenHelper.getScreenHeight().toInt()
}

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidth(): Int {
    return TKKMPSample.ScreenHelper.getScreenWidth().toInt()
}

@Composable
@OptIn(ExperimentalForeignApi::class)
actual fun getSafeAreaInsets() : Map<kotlin.Any?, *> {
    return TKKMPSample.ScreenHelper.getSafeAreaInsets()
}