package com.pixnpunk.settings.presentation.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

class SettingsExecutor(

) : CoroutineExecutor<SettingsStore.Intent, SettingsStore.Action, SettingsStore.State, SettingsStore.Message, SettingsStore.Label>() {

    override fun executeIntent(intent: SettingsStore.Intent) {
        super.executeIntent(intent)
    }

    override fun executeAction(action: SettingsStore.Action) {
        super.executeAction(action)
    }

    override fun dispose() {
        super.dispose()
    }
}