package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pixnpunk.settings.presentation.RealSettingsComponent
import me.zhanghai.compose.preference.preference
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.settings.presentation.generated.resources.Res
import quoroscope.features.settings.presentation.generated.resources.about_button_title
import quoroscope.features.settings.presentation.generated.resources.audio_button_title
import quoroscope.features.settings.presentation.generated.resources.general_button_title
import quoroscope.features.settings.presentation.generated.resources.pictures_button_title
import quoroscope.features.settings.presentation.generated.resources.text_button_title

@Composable
fun SettingsRootContent(
    isLocalSettings: Boolean,
    moveToChild: (RealSettingsComponent.ChildConfig) -> Unit
) {
    LazyColumn {
        preference(
            key = "generalPref",
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null
                )
            },
            title = { Text(stringResource(Res.string.general_button_title)) },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.General) }
        )
        preference(
            key = "textPref",
            icon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null
                )
            },
            title = { Text(stringResource(Res.string.text_button_title)) },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Text) }
        )
        preference(
            key = "imagePref",
            icon = {
                Icon(
                    imageVector = Icons.Filled.Image,
                    contentDescription = null
                )
            },
            title = { Text(stringResource(Res.string.pictures_button_title)) },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Pictures) }
        )
        preference(
            key = "soundPref",
            icon = {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null
                )
            },
            title = { Text(stringResource(Res.string.audio_button_title)) },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Audio) }
        )
        if (!isLocalSettings) {
            preference(
                key = "aboutPref",
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null
                    )
                },
                title = { Text(stringResource(Res.string.about_button_title)) },
                onClick = {}
            )
        }
    }
}