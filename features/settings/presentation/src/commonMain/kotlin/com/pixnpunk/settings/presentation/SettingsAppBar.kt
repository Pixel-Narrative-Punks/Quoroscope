package com.pixnpunk.settings.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.window.core.layout.WindowSizeClass
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.settings.presentation.generated.resources.Res
import quoroscope.features.settings.presentation.generated.resources.audio_button_title
import quoroscope.features.settings.presentation.generated.resources.general_button_title
import quoroscope.features.settings.presentation.generated.resources.global_root_button_title
import quoroscope.features.settings.presentation.generated.resources.local_root_button_title
import quoroscope.features.settings.presentation.generated.resources.pictures_button_title
import quoroscope.features.settings.presentation.generated.resources.text_button_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(
    activeComponent: SettingsComponent.Child,
    scrollBehavior: TopAppBarScrollBehavior,
    isLocalSettings: Boolean,
    gameTitle: String,
    onClickBack: () -> Unit
) {
    MediumTopAppBar(
        title = {
            Text(
                text = when (activeComponent) {
                    is SettingsComponent.Child.RootChild ->
                        if (isLocalSettings) {
                            stringResource(Res.string.local_root_button_title, gameTitle)
                        } else {
                            stringResource(Res.string.global_root_button_title)
                        }

                    is SettingsComponent.Child.AudioChild -> stringResource(Res.string.audio_button_title)

                    is SettingsComponent.Child.GeneralChild -> stringResource(Res.string.general_button_title)

                    is SettingsComponent.Child.PicturesChild -> stringResource(Res.string.pictures_button_title)

                    is SettingsComponent.Child.TextChild -> stringResource(Res.string.text_button_title)
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClickBack
            ) {
                when {
                    isLocalSettings && currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint(
                        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
                    ) -> {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}