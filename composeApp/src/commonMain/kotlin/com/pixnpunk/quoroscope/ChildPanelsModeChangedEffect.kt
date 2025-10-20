package com.pixnpunk.quoroscope

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanelsMode

@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
expect fun ChildPanelsModeChangedEffect(onModeChanged: (ChildPanelsMode) -> Unit)