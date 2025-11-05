package com.pixnpunk.dialogs.presentation

import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.pixnpunk.dialogs.presentation.content.DialogAddGameContent
import com.pixnpunk.dialogs.presentation.content.DialogEditGameContent
import com.pixnpunk.dialogs.presentation.content.DialogErrorContent
import com.pixnpunk.dialogs.presentation.content.DialogSelectColorContent
import com.pixnpunk.dialogs.presentation.content.DialogSelectGameContent
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.utils.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogsMainContent(
    component: DialogsComponent,
    settingsRepository: SettingsRepository
) {
    BasicAlertDialog(
        onDismissRequest = component.onDismissed
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {

            when (component.dialogsConfig.dialogsState) {
                DialogsState.DIALOG_ADD_GAME ->
                    DialogAddGameContent(
                        entry = component.dialogsConfig.unfilledEntry,
                        selectFile = component.selectFile,
                        selectFolder = component.selectFolder,
                        addGame = {
                            component.addNewEntry(it)
                            component.onDismissed()
                        },
                        buttonText = "Add game"
                    )
                DialogsState.DIALOG_DOWNLOAD_GAME ->
                    DialogAddGameContent(
                        entry = component.dialogsConfig.unfilledEntry,
                        selectFile = component.selectFile,
                        selectFolder = component.selectFolder,
                        addGame = {
                            component.downloadNewEntry(it)
                            component.onDismissed()
                        },
                        buttonText = "Download game"
                    )
                DialogsState.DIALOG_EDIT_GAME ->
                    DialogEditGameContent(
                        entry = component.dialogsConfig.unfilledEntry,
                        selectFile = component.selectFile,
                        editGame = {
                            component.editEntry(it)
                            component.onDismissed()
                        }
                    )
                DialogsState.DIALOG_ERROR_GAME, DialogsState.DIALOG_ERROR_ADDONS ->
                    DialogErrorContent(
                        state = component.dialogsConfig.dialogsState,
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
            }
        }
    }
}