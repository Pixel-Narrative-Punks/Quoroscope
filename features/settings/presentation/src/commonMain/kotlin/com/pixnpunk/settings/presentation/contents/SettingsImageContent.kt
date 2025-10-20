package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pixnpunk.settings.presentation.SettingsAppBar
import com.pixnpunk.settings.presentation.SettingsComponent
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.textFieldPreference

@Composable
fun SettingsImageContent(
    component: SettingsComponent,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        switchPreference(
            key = "useGameImagesPref",
            defaultValue = false,
            title = { Text("Disable game images") }
        )
        switchPreference(
            key = "useAutoImageWidthPref",
            defaultValue = true,
            title = { Text("Auto game image width") }
        )
        textFieldPreference(
            key = "manualImageWidthPref",
            defaultValue = "400",
            textToValue = { it.toInt() },
            title = { Text("Manual game image width") }
        )
        switchPreference(
            key = "useAutoImageHeightPref",
            defaultValue = true,
            title = { Text("Auto game image height") }
        )
        textFieldPreference(
            key = "manualImageHeightPref",
            defaultValue = "400",
            textToValue = { it.toInt() },
            title = { Text("Manual game image height") }
        )
        switchPreference(
            key = "useImmersImagesPref",
            defaultValue = false,
            title = { Text("All game images in dialog") }
        )
        switchPreference(
            key = "useGameImageDebug",
            defaultValue = false,
            title = { Text("Image path debugger") }
        )
    }
}