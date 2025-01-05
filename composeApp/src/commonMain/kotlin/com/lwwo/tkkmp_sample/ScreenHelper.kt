package com.lwwo.tkkmp_sample

import androidx.compose.runtime.Composable

@Composable
expect fun getScreenHeight(): Int

@Composable
expect fun getScreenWidth(): Int

@Composable
expect fun getSafeAreaInsets(): Map<kotlin.Any?, *>
