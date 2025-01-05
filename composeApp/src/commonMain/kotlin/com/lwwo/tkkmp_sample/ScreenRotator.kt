package com.lwwo.tkkmp_sample

import com.lwwo.tkkmp_sample.data.ScreenRotationType

interface ScreenRotator {
    fun rotateScreen(rotationType: ScreenRotationType)
}
