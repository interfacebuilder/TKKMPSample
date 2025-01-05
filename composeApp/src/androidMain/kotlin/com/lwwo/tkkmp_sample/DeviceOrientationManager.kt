package com.lwwo.tkkmp_sample

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.platform.LocalContext
import kotlin.math.atan2

/**
 * Android 実装:
 * - センサー(TYPE_ROTATION_VECTOR)を使って回転行列を取得し、roll/pitch/yawを算出する
 *
 * センサーの概要
 * https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview?hl=ja
 *
 */
class DeviceGyroManagerAndroid(private val context: Context) : DeviceGyroManager, SensorEventListener {

    private var listener: DeviceGyroListener? = null

    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    // 回転ベクトルセンサー
    private val rotationSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    // 回転行列用 (要素数16)
    private val rotationMatrix = FloatArray(16)
    // オイラー角度格納用 (要素数3: [azimuth(yaw), pitch, roll])
    private val orientationAngles = FloatArray(3)

    /**
     * リスナーを設定
     */
    override fun setListener(listener: DeviceGyroListener?) {
        this.listener = listener
    }

    /**
     * センサーの監視を開始
     */
    override fun startListening() {
        rotationSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME // いくつか試してみたがSENSOR_DELAY_GAMEが自然だった
            )
        }
    }

    /**
     * センサーの監視を停止
     */
    override fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    /**
     * センサー値が更新された場合に呼び出される
     *
     * 参考：
     * https://qiita.com/nakashimaakio/items/de9f7946673fe11a7d38
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
            // 回転ベクトルから回転行列を生成
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            // 回転行列からオイラー角度を取得
            SensorManager.getOrientation(rotationMatrix, orientationAngles)

            // orientationAngles は [yaw, pitch, roll] (単位はラジアン)
            val yaw = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            val pitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

            // リスナーへコールバック (左右回転は roll を見ることが多い)
            listener?.onOrientationChanged(
                GyroData(
                    roll = roll,
                    pitch = pitch,
                    yaw = yaw
                )
            )
        }
    }

    // センサーの精度変更
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }
}
