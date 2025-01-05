package com.lwwo.tkkmp_sample

import android.app.Application
import com.lwwo.tkkmp_sample.di.initKoin

class TKKMPSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
