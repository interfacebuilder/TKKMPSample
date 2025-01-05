package com.lwwo.tkkmp_sample.di

import com.lwwo.tkkmp_sample.data.InMemoryScreenRotationData
import com.lwwo.tkkmp_sample.data.ScreenRotationData
import com.lwwo.tkkmp_sample.viewmodel.ScreenRotationViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<ScreenRotationData> { InMemoryScreenRotationData() }
}

val viewModelModule = module {
    viewModel { ScreenRotationViewModel(get()) }
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule
        )
    }
}
