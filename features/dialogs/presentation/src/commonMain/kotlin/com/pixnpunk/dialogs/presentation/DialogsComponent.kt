package com.pixnpunk.dialogs.presentation

import com.pixnpunk.dto.GameEntry

sealed interface DialogsComponent {
    val dialogsConfig: DialogsConfig
    val addNewEntry: (GameEntry) -> Unit
    val downloadNewEntry: (GameEntry) -> Unit
    val selectFolder: () -> Unit
    val doPlayGame: (GameEntry, Int) -> Unit
    val onDismissed: () -> Unit
}