package com.pixnpunk.stock.presentation.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.pixnpunk.stock.domain.usecase.DeleteLocalGameEntries
import com.pixnpunk.stock.presentation.mvi.StockStore.Message.ChangeStateDeleteMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StockExecutor(
    private val deleteLocalGameEntries: DeleteLocalGameEntries
) : CoroutineExecutor<StockStore.Intent, StockStore.Action, StockStore.State, StockStore.Message, StockStore.Label>() {
    private val outScope = CoroutineScope(Dispatchers.Default)

    override fun executeIntent(intent: StockStore.Intent) {
        when (intent) {
            is StockStore.Intent.StartDeleteMode -> {
                dispatch(ChangeStateDeleteMode(true))
            }

            is StockStore.Intent.StopDeleteMode -> {
                dispatch(ChangeStateDeleteMode(false))
            }

            is StockStore.Intent.DeleteEntries -> {
                outScope.launch {
                    deleteLocalGameEntries.execute(intent.entries)
                    scope.launch { dispatch(ChangeStateDeleteMode(false)) }
                }
            }
        }
    }

    override fun executeAction(action: StockStore.Action) {
    }

    override fun dispose() {
        super.dispose()
        outScope.cancel()
    }
}