package com.pixnpunk.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

actual object ViewUtil {
    @Composable
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    actual fun calcWindowSizeClass(): WindowSizeClass {
        val activity = LocalActivity.current
        return if (activity != null) {
            calculateWindowSizeClass(activity)
        } else {
            val containerSize = LocalWindowInfo.current.containerSize
            val size = DpSize(containerSize.width.dp, containerSize.height.dp)
            return WindowSizeClass.calculateFromSize(size)
        }
    }
}