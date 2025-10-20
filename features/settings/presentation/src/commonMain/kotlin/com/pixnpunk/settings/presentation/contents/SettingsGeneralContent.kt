package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.pixnpunk.dto.NativeLibAuthors
import com.pixnpunk.settings.presentation.SettingsAppBar
import com.pixnpunk.settings.presentation.SettingsComponent
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.switchPreference

@Composable
fun SettingsGeneralContent(
    component: SettingsComponent,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        listPreference(
            key = "appThemePref",
            defaultValue = "auto",
            values = listOf("auto", "light", "dark"),
            valueToText = {
                AnnotatedString(
                    it.replaceFirstChar { txt ->
                        if (txt.isLowerCase()) txt.titlecase() else it
                    }
                )
            },
            title = { Text("Application theme") }
        )
        listPreference(
            key = "appLangPref",
            defaultValue = "en",
            values = listOf("en", "ru"),
            valueToText = {
                AnnotatedString(
                    when (it) {
                        "en" -> "English"
                        "ru" -> "Russian"
                        else -> it
                    }
                )
            },
            title = { Text("Application language") }
        )
        listPreference(
            key = "appLibVerPref",
            defaultValue = NativeLibAuthors.SEEDHARTHA.toString(),
            values = NativeLibAuthors.entries.map { it.toString() },
            valueToText = {
                AnnotatedString(
                    when (NativeLibAuthors.valueOf(it)) {
                        NativeLibAuthors.SEEDHARTHA -> "5.7.0 (by seedHarta)"
                        NativeLibAuthors.SONNIX -> "5.8.0 (by Sonnix)"
                        NativeLibAuthors.BYTE -> "5.9.3 (by Byte)"
                    }
                )
            },
            title = { Text("Application library version") }
        )
        listPreference(
            key = "appBinPref",
            defaultValue = 1000,
            values = listOf(1000, 1024),
            valueToText = {
                AnnotatedString(
                    when (it) {
                        1000 -> "B/KB/MB/GB/TB"
                        1024 -> "B/KiB/MiB/GiB/TiB"
                        else -> it.toString()
                    }
                )
            },
            title = { Text("Byte prefixes") }
        )
        listPreference(
            key = "countActsPref",
            defaultValue = 3,
            values = listOf(3, 4, 5, 6),
            title = { Text("Count visible action") }
        )
        switchPreference(
            key = "appImmersiveModePref",
            defaultValue = true,
            title = { Text("Immersive mode") }
        )
    }
}