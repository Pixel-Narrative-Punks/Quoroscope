package com.pixnpunk.quoroscope

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanelsMode

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun ChildPanelsModeChangedEffect(onModeChanged: (ChildPanelsMode) -> Unit) {
    val activity = LocalActivity.current

    if (activity != null) {
        val windowSize = calculateWindowSizeClass(activity)
        val mode = if (windowSize.widthSizeClass < Expanded) ChildPanelsMode.SINGLE else ChildPanelsMode.DUAL

        DisposableEffect(mode) {
            onModeChanged(mode)
            onDispose {}
        }
    }
}