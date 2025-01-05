package com.lwwo.tkkmp_sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.RasterLayer
import org.maplibre.android.style.sources.RasterSource
import org.maplibre.android.style.sources.TileSet
import kotlin.math.cos
import kotlin.math.sin

// 移動・回転 速度係数
const val ANDROID_FORWARD_SPEED_FACTOR: Double = 0.0003
const val ANDROID_ROTATE_SPEED_FACTOR: Double = 20.0

@Composable
actual fun MapComposable(
    forwardSliderValue: Float,
    rotateSliderValue: Float
) {
    val context = LocalContext.current

    // MapView の初期化
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { map ->
                // タイルセット設定
                val tileSet = TileSet("2.2.0", MapConstants.TILE_URL_TEMPLATE).apply {
                    minZoom = MapConstants.MIN_ZOOM
                    maxZoom = MapConstants.MAX_ZOOM
                    scheme = "xyz"
                }
                val rasterSource = RasterSource(MapConstants.TILE_SOURCE_ID, tileSet, MapConstants.TILE_SIZE_PX)
                val rasterLayer = RasterLayer(MapConstants.TILE_LAYER_ID, MapConstants.TILE_SOURCE_ID)

                val styleBuilder = Style.Builder()
                    .withSource(rasterSource)
                    .withLayer(rasterLayer)

                map.setStyle(styleBuilder) {
                    // 初期位置カメラ
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(MapConstants.INITIAL_LAT, MapConstants.INITIAL_LNG))
                        .zoom(MapConstants.INITIAL_ZOOM)
                        .tilt(MapConstants.INITIAL_PITCH)
                        .bearing(MapConstants.INITIAL_BEARING)
                        .build()

                    // ユーザー操作制限
                    map.uiSettings.apply {
                        isZoomGesturesEnabled = false
                        isTiltGesturesEnabled = false
                        // 回転は有効
                    }
                }
            }
        }
    }

    // 前後移動 + 回転の連続処理
    LaunchedEffect(forwardSliderValue, rotateSliderValue) {
        fun toMinusOneToOne(value: Float) = (value - 0.5f) * 2f

        while (isActive) {
            val forwardDir = toMinusOneToOne(forwardSliderValue)
            val rotateDir = toMinusOneToOne(rotateSliderValue)

            if (forwardDir == 0f && rotateDir == 0f) {
                break
            }

            val forwardStep = forwardDir * ANDROID_FORWARD_SPEED_FACTOR
            val rotateStep = rotateDir * ANDROID_ROTATE_SPEED_FACTOR

            mapView.getMapAsync { map ->
                val currentCamPos = map.cameraPosition
                val curLat = currentCamPos.target!!.latitude    // TODO:
                val curLng = currentCamPos.target!!.longitude   // TODO:
                val curBearing = currentCamPos.bearing

                // 新しいベアリング
                val newBearing = curBearing + rotateStep

                // 現在のベアリング方向に前後進
                val rad = Math.toRadians(newBearing)
                val dx = forwardStep * cos(rad)
                val dy = forwardStep * sin(rad)
                val newLat = curLat + dx
                val newLng = curLng + dy

                val newPos = CameraPosition.Builder()
                    .target(LatLng(newLat, newLng))
                    .zoom(MapConstants.INITIAL_ZOOM)
                    .tilt(MapConstants.INITIAL_PITCH)
                    .bearing(newBearing)
                    .build()

                map.animateCamera(CameraUpdateFactory.newCameraPosition(newPos))
            }

            delay(MapConstants.UPDATE_INTERVAL_MS)
        }
    }

    AndroidView(factory = { mapView })
}
