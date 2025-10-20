package com.pixnpunk.details.presentation.mvi

import com.arkivanov.mvikotlin.core.store.Store
import kotlinx.serialization.Serializable

sealed interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {
    @Serializable
    data object State

    sealed interface Intent

    sealed interface Message

    sealed interface Label

    sealed interface Action
}