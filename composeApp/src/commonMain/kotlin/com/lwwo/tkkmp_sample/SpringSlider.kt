package com.lwwo.tkkmp_sample

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * スライダーの向きを表すEnum。
 * vertical = 縦スライダー
 * horizontal = 横スライダー
 */
enum class SpringSliderType {
    vertical,
    horizontal
}

/**
 * SpringSlider
 * - ドラッグ中はスライダーが指に追従して動く
 * - ドラッグを離した時にスプリングのように中央(0.5f)に戻る
 * - スライダーの向きは SpringSliderType により縦・横を選択
 *
 * @param sliderType スライダーの向き (vertical, horizontal)
 * @param onValueChange スライダー値変更時のコールバック
 */
@Composable
@Preview
fun SpringSlider(
    sliderType: SpringSliderType = SpringSliderType.vertical,
    modifier: Modifier,
    onValueChange: (Float) -> Unit = {}
) {

    // アニメーション可能な値を保持するAnimatable
    val sliderValue = remember { Animatable(0.5f) }

    // コルーチンスコープ
    val coroutineScope = rememberCoroutineScope()

    // 向きに応じて回転角度を決定
    val rotationAngle = when (sliderType) {
        SpringSliderType.vertical -> 270f
        SpringSliderType.horizontal -> 0f
    }

    // 向きに応じて適切なレイアウト修飾子を設定
    val updatedModifier = when (sliderType) {
        SpringSliderType.vertical -> {
            modifier
                .rotate(rotationAngle)
        }
        SpringSliderType.horizontal -> {
            modifier
                .rotate(rotationAngle)
        }
    }


    Slider(
        value = sliderValue.value,
        onValueChange = { value ->
            // ドラッグ中はアニメーションを中断して値を更新
            coroutineScope.launch {
                sliderValue.snapTo(value)
            }
            onValueChange(value)
        },
        onValueChangeFinished = {
            // ドラッグを離したら中央にアニメーションで戻す
            coroutineScope.launch {
                sliderValue.animateTo(
                    targetValue = 0.5f,
                    animationSpec = tween(durationMillis = 300)
                )
                // アニメーション後にコールバックを呼ぶ
                onValueChange(0.5f)
            }
        },
        valueRange = 0f..1f,
        colors = SliderDefaults.colors(
            thumbColor = Color.Red,        // ツマミの色
            activeTrackColor = Color.Blue, // 有効トラックの色
            inactiveTrackColor = Color.Blue // 無効トラックの色
        ),
        modifier = updatedModifier

    )
}
