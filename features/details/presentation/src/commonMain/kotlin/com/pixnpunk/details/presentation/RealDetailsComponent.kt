package com.pixnpunk.details.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.pixnpunk.details.presentation.mvi.RealDetailsStore
import com.pixnpunk.dto.GameEntry

class RealDetailsComponent(
    private val componentContext: ComponentContext,
    override val entryShow: GameEntry,
    override val showSelectGameDialog: (GameEntry) -> Unit,
    override val showEditGameDialog: (GameEntry) -> Unit,
    override val showDownloadGameDialog: (GameEntry) -> Unit,
    override val onDismissed: () -> Unit
) : ComponentContext by componentContext, DetailsComponent {

    private val store = instanceKeeper.getStore {
        RealDetailsStore(DefaultStoreFactory()).create()
    }

}