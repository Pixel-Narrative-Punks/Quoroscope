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

@Composable
fun SettingsAudioContent(
    component: SettingsComponent,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        switchPreference(
            key = "useGameAudioPref",
            defaultValue = true,
            title = { Text("Play game audio") }
        )
        switchPreference(
            key = "useMuteGameVideoPref",
            defaultValue = false,
            title = { Text("Mute game video") }
        )
        switchPreference(
            key = "useGameAudioDebug",
            defaultValue = false,
            title = { Text("Audio path debugger") }
        )
    }
}