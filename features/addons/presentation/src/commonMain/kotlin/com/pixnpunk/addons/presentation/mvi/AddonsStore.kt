package com.pixnpunk.addons.presentation.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.pixnpunk.addons.domain.model.AddonData
import kotlinx.serialization.Serializable

sealed interface AddonsStore : Store<AddonsStore.Intent, AddonsStore.State, AddonsStore.Label> {
    @Serializable
    data class State(
        val addonsList: List<AddonData> = listOf()
    )

    sealed interface Intent

    sealed interface Message {
        data class UpdateAddonsList(val addonsList: List<AddonData>) : Message
    }

    sealed interface Label

    sealed interface Action {
        data object InitialLoadAddonsList : Action
    }
}