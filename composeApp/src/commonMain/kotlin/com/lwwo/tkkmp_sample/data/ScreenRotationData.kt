package com.lwwo.tkkmp_sample.data


enum class ScreenRotationType {
    portrait,
    landscape
}


interface ScreenRotationData {

    fun setScreenRotation(type: ScreenRotationType)
    fun getScreenRotation(): ScreenRotationType

}

class InMemoryScreenRotationData : ScreenRotationData {

    private var storedValue: ScreenRotationType = ScreenRotationType.portrait

    override fun getScreenRotation(): ScreenRotationType {
        return storedValue
    }

    override fun setScreenRotation(type: ScreenRotationType) {
        storedValue = type
    }

}
