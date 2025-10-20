package com.pixnpunk.settings.presentation.contents

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.pixnpunk.dto.FontTypeface
import com.pixnpunk.settings.presentation.SettingsComponent
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.switchPreference

@Composable
fun SettingsTextContent(
    component: SettingsComponent,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        listPreference(
            key = "fontStylePref",
            defaultValue = FontTypeface.DEFAULT.toString(),
            values = FontTypeface.entries.map { it.toString() },
            valueToText = {
                AnnotatedString(
                    when (FontTypeface.valueOf(it)) {
                        FontTypeface.DEFAULT -> "Default"
                        FontTypeface.SANS_SERIF -> "Sans-Serif"
                        FontTypeface.SERIF -> "Serif"
                        FontTypeface.MONOSPACE -> "Monospace"
                    }
                )
            },
            title = { Text("Font style") }
        )
        listPreference(
            key = "fontSizePref",
            defaultValue = 16,
            values = listOf(12, 14, 16, 18, 20, 22),
            title = { Text("Font size") }
        )
        switchPreference(
            key = "useGameTextColorPref",
            defaultValue = true,
            title = { Text("Text color from game") }
        )
        switchPreference(
            key = "useGameBackColorPref",
            defaultValue = true,
            title = { Text("Background color from game") }
        )
        switchPreference(
            key = "useGameLinkColorPref",
            defaultValue = true,
            title = { Text("Link color from game") }
        )
        switchPreference(
            key = "useGameExecStringPref",
            defaultValue = false,
            title = { Text("Use command line") }
        )
    }
}