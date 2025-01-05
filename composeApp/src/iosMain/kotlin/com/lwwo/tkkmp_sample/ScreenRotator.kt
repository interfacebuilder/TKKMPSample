package com.lwwo.tkkmp_sample

import com.lwwo.tkkmp_sample.data.ScreenRotationType
import kotlinx.cinterop.ExperimentalForeignApi


/**
 * iOS 向けの ScreenRotator 実装
 */
class ScreenRotatorIOS : ScreenRotator {
    @OptIn(ExperimentalForeignApi::class)
    override fun rotateScreen(rotationType: ScreenRotationType) {
        // Swift側の記述をこちらに書いても良いがNative呼び出しの訓練も兼ねて
        TKKMPSample.ScreenRotator.rotateScreenWithIsPortrait(rotationType == ScreenRotationType.portrait)
    }
}
