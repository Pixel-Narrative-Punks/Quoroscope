package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.zhanghai.compose.preference.switchPreference
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.settings.presentation.generated.resources.Res
import quoroscope.features.settings.presentation.generated.resources.audio_path_switch_title
import quoroscope.features.settings.presentation.generated.resources.audio_play_switch_title
import quoroscope.features.settings.presentation.generated.resources.mute_video_switch_title

@Composable
fun SettingsAudioContent(
) {
    LazyColumn {
        switchPreference(
            key = "useGameAudioPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null
                )
            },
            defaultValue = true,
            title = { Text(stringResource(Res.string.audio_play_switch_title)) }
        )
        switchPreference(
            key = "useMuteGameVideoPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null
                )
            },
            defaultValue = false,
            title = { Text(stringResource(Res.string.mute_video_switch_title)) }
        )
        switchPreference(
            key = "useGameAudioDebug",
            icon = {
                Icon(
                    imageVector = Icons.Default.AudioFile,
                    contentDescription = null
                )
            },
            defaultValue = false,
            title = { Text(stringResource(Res.string.audio_path_switch_title)) }
        )
    }
}