package com.pixnpunk.details.presentation.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import com.pixnpunk.details.presentation.DetailsComponent
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.details.presentation.generated.resources.Res
import quoroscope.features.details.presentation.generated.resources.back_button_description

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppBar(
    component: DetailsComponent
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(
                onClick = component.onDismissed
            ) {
                if (
                    currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.back_button_description)
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}