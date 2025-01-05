package com.lwwo.tkkmp_sample

/**
 * デバイスの回転や傾き情報を保持するデータクラス
 * - roll: x軸回転 (車でいうとハンドルの左右)
 * - pitch: y軸回転 (上下の傾き)
 * - yaw: z軸回転 (方位など、水平面上での回転)
 */
data class GyroData(
    val roll: Float,
    val pitch: Float,
    val yaw: Float
)

/**
 * センサーからの回転情報を受け取るためのリスナー
 */
interface DeviceGyroListener {
    /**
     * センサー更新時に呼び出される
     * @param orientation 最新の回転・傾き情報
     */
    fun onOrientationChanged(orientation: GyroData)
}

/**
 * センサーを開始/停止し、回転情報をコールバックで通知するクラス
 * 実装はプラットフォームごとに行う
 */
interface DeviceGyroManager {
    /**
     * センサーからのリスナーを設定する
     */
    fun setListener(listener: DeviceGyroListener?)

    /**
     * センサーの計測を開始する
     */
    fun startListening()

    /**
     * センサーの計測を停止する
     */
    fun stopListening()
}

