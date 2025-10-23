package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pixnpunk.settings.presentation.RealSettingsComponent
import com.pixnpunk.settings.presentation.SettingsComponent
import me.zhanghai.compose.preference.preference

@Composable
fun SettingsRootContent(
    moveToChild: (RealSettingsComponent.ChildConfig) -> Unit
) {
    LazyColumn {
        preference(
            key = "generalPref",
            title = { Text("Basic") },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.General) }
        )
        preference(
            key = "textPref",
            title = { Text("Text") },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Text) }
        )
        preference(
            key = "imagePref",
            title = { Text("Pictures") },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Pictures) }
        )
        preference(
            key = "soundPref",
            title = { Text("Audio") },
            onClick = { moveToChild(RealSettingsComponent.ChildConfig.Audio) }
        )
        preference(
            key = "aboutPref",
            title = { Text("About app") },
            onClick = {}
        )
    }
}