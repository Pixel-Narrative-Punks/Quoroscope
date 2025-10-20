package com.pixnpunk.details.presentation.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class DetailsExecutor(

) : CoroutineExecutor<DetailsStore.Intent, DetailsStore.Action, DetailsStore.State, DetailsStore.Message, DetailsStore.Label>() {
    private val outScope = CoroutineScope(Dispatchers.Default)

    override fun executeIntent(intent: DetailsStore.Intent) {
        super.executeIntent(intent)
    }

    override fun executeAction(action: DetailsStore.Action) {
        super.executeAction(action)
    }

    override fun dispose() {
        super.dispose()
        outScope.cancel()
    }
}