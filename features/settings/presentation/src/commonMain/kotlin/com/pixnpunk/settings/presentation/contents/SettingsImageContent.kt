package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.zhanghai.compose.preference.rememberPreferenceState
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.textFieldPreference

@Composable
fun SettingsImageContent(
) {
    val autoImageWidthPreferenceState = rememberPreferenceState("useAutoImageWidthPref", true)
    val autoImageHeightPreferenceState = rememberPreferenceState("useAutoImageHeightPref", true)

    LazyColumn {
        switchPreference(
            key = "useGameImagesPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null
                )
            },
            defaultValue = false,
            title = { Text("Disable game images") }
        )
        switchPreference(
            key = "useAutoImageWidthPref",
            icon = {
//                Icon(
//                    imageVector = Icons.Default.WidthFull,
//                    contentDescription = null
//                )
            },
            rememberState = { autoImageWidthPreferenceState },
            defaultValue = true,
            title = { Text("Auto game image width") }
        )
        textFieldPreference(
            key = "manualImageWidthPref",
            enabled = { !autoImageWidthPreferenceState.value },
            defaultValue = "400",
            textToValue = { it.toInt() },
            title = { Text("Manual game image width") }
        )
        switchPreference(
            key = "useAutoImageHeightPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.Height,
                    contentDescription = null
                )
            },
            rememberState = { autoImageHeightPreferenceState },
            defaultValue = true,
            title = { Text("Auto game image height") }
        )
        textFieldPreference(
            key = "manualImageHeightPref",
            enabled = { !autoImageHeightPreferenceState.value },
            defaultValue = "400",
            textToValue = { it.toInt() },
            title = { Text("Manual game image height") }
        )
        switchPreference(
            key = "useImmersImagesPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.OpenInFull,
                    contentDescription = null
                )
            },
            defaultValue = false,
            title = { Text("All game images in dialog") }
        )
        switchPreference(
            key = "useGameImageDebug",
            icon = {
                Icon(
                    imageVector = Icons.Default.BrokenImage,
                    contentDescription = null
                )
            },
            defaultValue = false,
            title = { Text("Image path debugger") }
        )
    }
}