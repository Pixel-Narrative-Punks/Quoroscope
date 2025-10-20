package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pixnpunk.settings.presentation.RealSettingsComponent
import com.pixnpunk.settings.presentation.SettingsComponent
import me.zhanghai.compose.preference.preference

@Composable
fun SettingsRootContent(
    component: SettingsComponent,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        preference(
            key = "generalPref",
            title = { Text("Basic") },
            onClick = { component.moveToChild(RealSettingsComponent.ChildConfig.General) }
        )
        preference(
            key = "textPref",
            title = { Text("Text") },
            onClick = { component.moveToChild(RealSettingsComponent.ChildConfig.Text) }
        )
        preference(
            key = "imagePref",
            title = { Text("Pictures") },
            onClick = { component.moveToChild(RealSettingsComponent.ChildConfig.Pictures) }
        )
        preference(
            key = "soundPref",
            title = { Text("Audio") },
            onClick = { component.moveToChild(RealSettingsComponent.ChildConfig.Audio) }
        )
        preference(
            key = "aboutPref",
            title = { Text("About app") },
            onClick = {}
        )
    }
}