package com.pixnpunk.settings.presentation.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

class RealSettingsStore(
    private val storeFactory: StoreFactory
) {
    internal fun create(): SettingsStore = SettingsImpl()

    private inner class SettingsImpl : SettingsStore,
        Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> by storeFactory.create(
            name = "SettingsStore",
            initialState = SettingsStore.State,
            executorFactory = { SettingsExecutor() }
        )
}