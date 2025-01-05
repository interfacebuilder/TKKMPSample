package com.lwwo.tkkmp_sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lwwo.tkkmp_sample.data.ScreenRotationData
import com.lwwo.tkkmp_sample.data.ScreenRotationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScreenRotationViewModel(
    private val screenRotationData: ScreenRotationData
) : ViewModel() {

    private val _screenRotation = MutableStateFlow(screenRotationData.getScreenRotation())
    val screenRotation: StateFlow<ScreenRotationType> get() = _screenRotation

    fun setScreenRotation(type: ScreenRotationType) {
        viewModelScope.launch {
            screenRotationData.setScreenRotation(type)
            _screenRotation.value = type
        }
    }
}
