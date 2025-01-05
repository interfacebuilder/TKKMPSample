package com.lwwo.tkkmp_sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lwwo.tkkmp_sample.data.ScreenRotationType
import com.lwwo.tkkmp_sample.viewmodel.ScreenRotationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun MainComposable(deviceGyroManager: DeviceGyroManager, screenRotator: ScreenRotator) {

    /**
     * マップ移動用（前後進行）のスライダー値
     *   0.5 を中心として上下（0.0～1.0）
     *   中心より上なら前進 (0.5 < value <= 1.0)
     *   中心より下なら後進 (0.0 <= value < 0.5)
     */
    var forwardSliderValue by remember { mutableStateOf(0.5f) }

    /**
     * マップ回転用（左右回転）のスライダー値
     *   0.5 を中心として左右（0.0～1.0）
     *   中心より右なら右回転 (0.5 < value <= 1.0)
     *   中心より左なら左回転 (0.0 <= value < 0.5)
     */
    var rotateSliderValue by remember { mutableStateOf(0.5f) }

    // マップ移動のスライダーが利用されているかどうか
    var forwardDriveEnabled by remember { mutableStateOf(false) }

    // 回転ボタンを押したときの状態保持
    val viewModel = koinViewModel<ScreenRotationViewModel>()
    val screenRotation by viewModel.screenRotation.collectAsState()

    // deviceGyroManager のリスナーを設定し、ライフサイクルを管理
    DisposableEffect(Unit) {
        // リスナーの定義
        val listener = object : DeviceGyroListener {
            override fun onOrientationChanged(orientation: GyroData) {
                // ロール角度をスライダーの値にマッピング
                // ロール角度が -90°～+90° の範囲と仮定
                val rollDegrees = if (screenRotation == ScreenRotationType.landscape) {
                    orientation.pitch // 横向きの場合、pitchを使う
                } else {
                    orientation.roll
                }
                val mappedValue = ((rollDegrees + 90f) / 180f).coerceIn(0f, 1f)
                // forwardDriveEnabled が有効の時のみ回転を許可する
                if (forwardDriveEnabled) {
                    rotateSliderValue = mappedValue
                }
            }
        }

        deviceGyroManager.setListener(listener)
        // センサーの監視を開始
        deviceGyroManager.startListening()

        // Composable が破棄される際にセンサーの監視を停止
        onDispose {
            deviceGyroManager.stopListening()
            deviceGyroManager.setListener(null)
        }
    }


    MaterialTheme {

        MapComposable(
            forwardSliderValue = forwardSliderValue,
            rotateSliderValue = rotateSliderValue
        )

        // 画面サイズと縦・横向きの判定
        val screenWidth = getScreenWidth()
        val screenHeight = getScreenHeight()
        val shortSide = minOf(screenWidth, screenHeight)
        val isPortrait = screenHeight > screenWidth

        val safeArea = getSafeAreaInsets()
        val safeAreaBottom = safeArea["bottom"] as Double
        val safeAreaRight = safeArea["right"] as Double
        val safeAreaTop = safeArea["top"] as Double
        val safeAreaLeft = safeArea["left"] as Double

        // スライダー関連
        val virticalSliderSize = (0.8f * shortSide).dp  // 短辺の80%の長さにする

        Box(modifier = Modifier.fillMaxSize()) {
            // 右下スライダー（前後進行）
            // 縦配置にしたいが用意されてなさそう。試行錯誤の末、
            // 設定したい高さ（ここではデバイスサイズ幅*0.8）の正方形を作成・回転・中央配置させてからoffsetで位置調整する
            SpringSlider(
                SpringSliderType.vertical,
                modifier = Modifier
                    .width(virticalSliderSize)
                    .height(virticalSliderSize)
                    .align(Alignment.CenterEnd)
                    .offset(x = (virticalSliderSize / 2) - (safeAreaRight + 32).dp,
                            y = if (isPortrait) 128.dp else 0.dp ),
                onValueChange = { value ->
                    forwardSliderValue = value
                    // 右スライダーが動いてる時のみ、デバイスによる回転を有効にする
                    if (value == 0.5f) {
                        forwardDriveEnabled = false
                        rotateSliderValue = 0.5f
                    } else {
                        forwardDriveEnabled = true
                    }
                }
            )

            // 左下スライダー（左右回転）
            SpringSlider(
                SpringSliderType.horizontal,
                modifier = Modifier
                    .width((0.6f * shortSide).dp)
                    .height(10.dp)
                    .align(Alignment.BottomStart)
                    .offset(x =  if (isPortrait) 0.dp else safeAreaRight.dp,
                            y = (-(safeAreaBottom + 32).dp)),
                onValueChange = { value ->
                    rotateSliderValue = value
                }
            )

            // 回転ボタン
            OutlinedButton(
                onClick = {
                    if (screenRotation == ScreenRotationType.portrait) {
                        screenRotator.rotateScreen(ScreenRotationType.landscape)
                        viewModel.setScreenRotation(ScreenRotationType.landscape)
                    } else {
                        screenRotator.rotateScreen(ScreenRotationType.portrait)
                        viewModel.setScreenRotation(ScreenRotationType.portrait)
                    }

                },
                border = BorderStroke(1.dp, Color.Blue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = safeAreaLeft.dp + 8.dp, y = safeAreaTop.dp)
            ) {
                if (screenRotation == ScreenRotationType.landscape) Text("縦向きにする↑") else Text("横向きにする→")
            }

        }

    }
}

