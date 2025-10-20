package com.pixnpunk.dialogs.presentation

import com.arkivanov.decompose.ComponentContext
import com.pixnpunk.dto.GameEntry

class RealDialogsComponent(
    private val componentContext: ComponentContext,
    override val dialogsConfig: DialogsConfig,
    override val addNewEntry: (GameEntry) -> Unit,
    override val downloadNewEntry: (GameEntry) -> Unit,
    override val selectFolder: () -> Unit,
    override val doPlayGame: (GameEntry, Int) -> Unit,
    override val onDismissed: () -> Unit
) : ComponentContext by componentContext, DialogsComponent