package com.pixnpunk.dialogs.presentation

import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.GameEntry
import kotlinx.serialization.Serializable

@Serializable
data class DialogsConfig(
    val dialogsState: DialogsState,
    val colorPickerState: ColorPickerState? = null,
    val unfilledEntry: GameEntry = GameEntry()
)