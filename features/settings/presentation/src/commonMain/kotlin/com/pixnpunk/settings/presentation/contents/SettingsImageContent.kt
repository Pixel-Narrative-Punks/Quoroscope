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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.pixnpunk.dto.InputState
import me.zhanghai.compose.preference.switchPreference
import me.zhanghai.compose.preference.twoTargetSwitchPreference
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.settings.presentation.generated.resources.Res
import quoroscope.features.settings.presentation.generated.resources.dialog_images_switch_title
import quoroscope.features.settings.presentation.generated.resources.disable_images_switch_title
import quoroscope.features.settings.presentation.generated.resources.image_height_twotarswitch_summary
import quoroscope.features.settings.presentation.generated.resources.image_height_twotarswitch_title
import quoroscope.features.settings.presentation.generated.resources.image_path_switch_title
import quoroscope.features.settings.presentation.generated.resources.image_width_twotarswitch_summary
import quoroscope.features.settings.presentation.generated.resources.image_width_twotarswitch_title

@Composable
fun SettingsImageContent(
    showInputDialog: (InputState) -> Unit
) {
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
            title = { Text(stringResource(Res.string.disable_images_switch_title)) }
        )
        twoTargetSwitchPreference(
            key = "useAutoImageWidthPref",
            icon = {
                Icon(
                    modifier = Modifier.rotate(90f),
                    imageVector = Icons.Default.Height,
                    contentDescription = null
                )
            },
            defaultValue = true,
            title = {
                Text(
                    stringResource(
                        Res.string.image_width_twotarswitch_title,
                        if (it) "Auto" else "Manual"
                    )
                )
            },
            summary = {
                if (!it) {
                    Text(stringResource(Res.string.image_width_twotarswitch_summary))
                }
            },
            onClick = {
                if (!it) {
                    showInputDialog(InputState.INPUT_IMAGE_WIDTH)
                }
            }
        )
        twoTargetSwitchPreference(
            key = "useAutoImageHeightPref",
            icon = {
                Icon(
                    imageVector = Icons.Default.Height,
                    contentDescription = null
                )
            },
            defaultValue = true,
            title = {
                Text(
                    stringResource(
                        Res.string.image_height_twotarswitch_title,
                        if (it) "Auto" else "Manual"
                    )
                )
            },
            summary = {
                if (!it) {
                    Text(stringResource(Res.string.image_height_twotarswitch_summary))
                }
            },
            onClick = {
                if (!it) {
                    showInputDialog(InputState.INPUT_IMAGE_HEIGHT)
                }
            }
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
            title = { Text(stringResource(Res.string.dialog_images_switch_title)) }
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
            title = { Text(stringResource(Res.string.image_path_switch_title)) }
        )
    }
}