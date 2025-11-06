package com.pixnpunk.dialogs.presentation

import androidx.compose.ui.graphics.Color
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.dto.InputState

sealed interface DialogsComponent {
    val dialogsConfig: DialogsConfig
    val addNewEntry: (GameEntry) -> Unit
    val editEntry: (GameEntry) -> Unit
    val downloadNewEntry: (GameEntry) -> Unit
    val selectFolder: () -> Unit
    val selectFile: () -> Unit
    val selectColor: (Pair<ColorPickerState, Color>) -> Unit
    val inputValue: (Pair<InputState, String>) -> Unit
    val doPlayGame: (GameEntry, Int) -> Unit
    val onDismissed: () -> Unit
}