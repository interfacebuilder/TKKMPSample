package com.lwwo.tkkmp_sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import platform.UIKit.UIView
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// 移動・回転 速度係数
const val IOS_FORWARD_SPEED_FACTOR: Double = 0.00004
const val IOS_ROTATE_SPEED_FACTOR: Double = 5.0

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapComposable(
    forwardSliderValue: Float,
    rotateSliderValue: Float
) {
    // Swift 側のラッパーを生成
    val mapViewWrapper = remember { TKKMPSample.SwiftMapViewWrapper() }

    // 初期スタイルセットアップ
    LaunchedEffect(Unit) {
        mapViewWrapper.setupMapStyleWithTileURLTemplate(
            tileURLTemplate = MapConstants.TILE_URL_TEMPLATE,
            minZoom = MapConstants.MIN_ZOOM,
            maxZoom = MapConstants.MAX_ZOOM,
            tileSize = MapConstants.TILE_SIZE_PX.toLong(),
            sourceID = MapConstants.TILE_SOURCE_ID,
            layerID = MapConstants.TILE_LAYER_ID,
            initialLatitude = MapConstants.INITIAL_LAT,
            initialLongitude = MapConstants.INITIAL_LNG,
            initialZoom = MapConstants.INITIAL_ZOOM,
            initialBearing = MapConstants.INITIAL_BEARING,
            initialPitch = MapConstants.INITIAL_PITCH
        )
    }

    // スライダー値で前後移動・回転
    LaunchedEffect(forwardSliderValue, rotateSliderValue) {
    fun toMinusOneToOne(value: Float) = (value - 0.5f) * 2.0f

        while (isActive) {
            val forwardDir = toMinusOneToOne(forwardSliderValue)
            val rotateDir = toMinusOneToOne(rotateSliderValue)

            if (forwardDir == 0f && rotateDir == 0f) {
                break
            }
            // iOS から現在のカメラ情報を取得
            val currentCamMap = mapViewWrapper.getCurrentCameraPosition()
            val curLat = (currentCamMap["latitude"] as? Double) ?: MapConstants.INITIAL_LAT
            val curLng = (currentCamMap["longitude"] as? Double) ?: MapConstants.INITIAL_LNG
            val curBearing = (currentCamMap["bearing"] as? Double) ?: MapConstants.INITIAL_BEARING
            val curZoom = (currentCamMap["zoom"] as? Double) ?: MapConstants.INITIAL_ZOOM
            val curPitch = (currentCamMap["pitch"] as? Double) ?: MapConstants.INITIAL_PITCH

            // 移動・回転量を計算
            val forwardStep = forwardDir * IOS_FORWARD_SPEED_FACTOR
            val rotateStep = rotateDir * IOS_ROTATE_SPEED_FACTOR

            // 回転
            val newBearingRaw = curBearing + rotateStep
            // 0..360度に正規化
            val newBearing = ((newBearingRaw % 360.0) + 360.0) % 360.0

            // 移動
            val bearingRad = newBearing * PI / 180.0
            val dx = forwardStep * cos(bearingRad)
            val dy = forwardStep * sin(bearingRad)
            val newLat = curLat + dx
            val newLng = curLng + dy

            // カメラ更新
            mapViewWrapper.setCenterWithLatitude(
                latitude = newLat,
                longitude = newLng,
                zoom = curZoom,
                heading = newBearing,
                pitch = curPitch
            )

            delay(MapConstants.UPDATE_INTERVAL_MS)
        }
    }



    // UIKitViewでMLNMapViewを表示
    UIKitView(
        factory = {
            mapViewWrapper.getMapView() as UIView
        },

        modifier = Modifier.fillMaxSize()


    )

}
