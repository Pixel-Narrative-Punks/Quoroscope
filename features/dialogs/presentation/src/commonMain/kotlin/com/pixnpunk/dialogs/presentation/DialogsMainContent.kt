package com.pixnpunk.dialogs.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.pixnpunk.dialogs.presentation.content.DialogAddGameContent
import com.pixnpunk.dialogs.presentation.content.DialogEditGameContent
import com.pixnpunk.dialogs.presentation.content.DialogErrorContent
import com.pixnpunk.dialogs.presentation.content.DialogInputValueContent
import com.pixnpunk.dialogs.presentation.content.DialogOutputValueContent
import com.pixnpunk.dialogs.presentation.content.DialogSelectColorContent
import com.pixnpunk.dialogs.presentation.content.DialogSelectGameContent
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.InputState
import com.pixnpunk.utils.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogsMainContent(
    component: DialogsComponent,
    settingsRepository: SettingsRepository
) {
    val state = component.dialogsConfig.dialogsState
    if (state == DialogsState.DIALOG_ADD_GAME
        || state == DialogsState.DIALOG_EDIT_GAME) {
        BasicAlertDialog(
            onDismissRequest = component.onDismissed,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                when (state) {
                    DialogsState.DIALOG_ADD_GAME ->
                        DialogAddGameContent(
                            entry = component.dialogsConfig.unfilledEntry,
                            selectFile = component.selectFile,
                            deselectFile = { component.deselectFile(it) },
                            selectDir = component.selectFolder,
                            deselectDir = component.deselectDir,
                            selectFiles = component.selectFiles,
                            addGame = {
                                component.addNewEntry(it)
                                component.onDismissed()
                            },
                            onDismiss = component.onDismissed,
                            isAddLocalGame = true,
                            buttonText = "Add game"
                        )

                    DialogsState.DIALOG_EDIT_GAME ->
                        DialogEditGameContent(
                            entry = component.dialogsConfig.unfilledEntry,
                            selectFile = component.selectFile,
                            deselectFile = { component.deselectFile(it) },
                            selectFiles = component.selectFiles,
                            editGame = {
                                component.editEntry(it)
                                component.onDismissed()
                            }
                        )
                    else -> {}
                }
            }
        }
    } else {
        BasicAlertDialog(
            onDismissRequest = component.onDismissed,
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                when (state) {
                    DialogsState.DIALOG_DOWNLOAD_GAME ->
                        DialogAddGameContent(
                            entry = component.dialogsConfig.unfilledEntry,
                            selectFile = component.selectFile,
                            deselectFile = component.deselectFile,
                            selectDir = component.selectFolder,
                            deselectDir = component.deselectDir,
                            selectFiles = component.selectFiles,
                            addGame = {
                                component.downloadNewEntry(it)
                                component.onDismissed()
                            },
                            onDismiss = component.onDismissed,
                            isAddLocalGame = false,
                            buttonText = "Download game"
                        )

                    DialogsState.DIALOG_ERROR_GAME, DialogsState.DIALOG_ERROR_ADDONS ->
                        DialogErrorContent(
                            state = state,
                            gameTitle = component.dialogsConfig.unfilledEntry.title,
                            onDismiss = component.onDismissed
                        )

                    DialogsState.DIALOG_SELECT_RUN_GAME -> DialogSelectGameContent(component)
                    DialogsState.DIALOG_SELECT_COLOR -> {
                        val state = component.dialogsConfig.colorPickerState
                        if (state != null) {
                            val settings = settingsRepository.settingsFlow.value
                            DialogSelectColorContent(
                                selectColor = { component.selectColor(Pair(state, it)) },
                                initialColor = Color(
                                    when (state) {
                                        ColorPickerState.COLOR_TEXT -> settings.textColor
                                        ColorPickerState.COLOR_BACK -> settings.backColor
                                        ColorPickerState.COLOR_LINK -> settings.linkColor
                                    }
                                ),
                                onDismiss = component.onDismissed
                            )
                        }
                    }

                    DialogsState.DIALOG_INPUT_VALUE -> {
                        val state = component.dialogsConfig.inputState
                        if (state != null) {
                            val settings = settingsRepository.settingsFlow.value
                            DialogInputValueContent(
                                initialValue = "${
                                    when (state) {
                                        InputState.INPUT_IMAGE_WIDTH -> settings.imageWidth
                                        InputState.INPUT_IMAGE_HEIGHT -> settings.imageHeight
                                    }
                                }",
                                label = {
                                    Text(
                                        when (state) {
                                            InputState.INPUT_IMAGE_WIDTH -> "Image width"
                                            InputState.INPUT_IMAGE_HEIGHT -> "Image height"
                                        }
                                    )
                                },
                                inputValue = { component.inputValue(state to it) },
                                keyboardOptions =
                                    when (state) {
                                        InputState.INPUT_IMAGE_WIDTH, InputState.INPUT_IMAGE_HEIGHT ->
                                            KeyboardOptions(keyboardType = KeyboardType.Number)
                                    },
                                onDismiss = component.onDismissed
                            )
                        }
                    }

                    DialogsState.DIALOG_OUTPUT_VALUE -> {
                        val state = component.dialogsConfig.outputState
                        if (state != null) {
                            DialogOutputValueContent(
                                message = component.dialogsConfig.outputValue,
                                onConfirm = { component.confirmMessage(state) },
                                onDismiss = component.onDismissed
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}