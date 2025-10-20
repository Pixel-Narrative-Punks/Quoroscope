package com.pixnpunk.quoroscope.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.pixnpunk.dto.GameEntry
import kotlinx.serialization.Serializable

sealed interface RootStore : Store<RootStore.Intent, RootStore.State, RootStore.Label> {
    @Serializable
    data object State

    sealed interface Intent {
        data class AddNewGame(val unfilledEntry: GameEntry) : Intent
        data class DownloadNewGame(val entry: GameEntry) : Intent
        data object StartSenderGameSetting : Intent
    }

    sealed interface Message

    sealed interface Label

    sealed interface Action
}