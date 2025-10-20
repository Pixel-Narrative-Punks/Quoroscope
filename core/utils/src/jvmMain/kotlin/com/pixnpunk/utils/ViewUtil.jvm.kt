package com.pixnpunk.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

actual object ViewUtil {
    @Composable
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    actual fun calcWindowSizeClass() = calculateWindowSizeClass()
}