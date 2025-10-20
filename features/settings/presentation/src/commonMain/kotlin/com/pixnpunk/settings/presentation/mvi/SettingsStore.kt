package com.pixnpunk.settings.presentation.mvi

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.serialization.Serializable

sealed interface SettingsStore : Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {
    @Serializable
    data object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

    sealed interface Action
}