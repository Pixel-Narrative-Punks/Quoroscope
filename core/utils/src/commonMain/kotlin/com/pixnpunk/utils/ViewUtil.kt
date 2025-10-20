package com.pixnpunk.utils

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable

expect object ViewUtil {
    @Composable
    fun calcWindowSizeClass(): WindowSizeClass
}