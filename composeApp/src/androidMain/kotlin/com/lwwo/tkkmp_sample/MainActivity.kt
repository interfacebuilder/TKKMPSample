package com.lwwo.tkkmp_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import org.maplibre.android.MapLibre

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MapLibreの初期化
        MapLibre.getInstance(
            this
        )

        setContent {
            val deviceGyroManager = remember {
                DeviceGyroManagerAndroid(this)
            }
            val screenRotator = remember {
                ScreenRotatorAndroid(this )
            }
            MainComposable(deviceGyroManager, screenRotator)
        }
    }
}
