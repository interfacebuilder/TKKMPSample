package com.lwwo.tkkmp_sample

import platform.CoreMotion.CMAttitude
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue
import kotlin.math.PI

class DeviceGyroManageriOS : DeviceGyroManager {

    private var motionManager: CMMotionManager? = null
    private var listener: DeviceGyroListener? = null

    override fun setListener(listener: DeviceGyroListener?) {
        this.listener = listener
    }

    override fun startListening() {
        motionManager = CMMotionManager().apply {
            deviceMotionUpdateInterval = 0.02 // 20ms
            startDeviceMotionUpdatesToQueue(NSOperationQueue.mainQueue) { deviceMotion, error ->
                if (error == null && deviceMotion != null) {
                    val attitude: CMAttitude = deviceMotion.attitude

                    val yaw = toDegrees(attitude.yaw)
                    val pitch = toDegrees(attitude.pitch)
                    val roll = toDegrees(attitude.roll)

                    // リスナーに通知
                    listener?.onOrientationChanged(
                        GyroData(
                            roll = roll.toFloat(),
                            pitch = pitch.toFloat(),
                            yaw = yaw.toFloat()
                        )
                    )
                }
            }
        }
    }

    override fun stopListening() {
        motionManager?.stopDeviceMotionUpdates()
        motionManager = null
    }

    /**
     * ラジアンを度に変換
     */
    private fun toDegrees(value: Double): Double {
        return value * (180.0 / PI)
    }
}
