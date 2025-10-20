package com.pixnpunk.quoroscope.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.pixnpunk.addons.AddonsUtils
import com.pixnpunk.stock.domain.usecase.FillAndInsertEntryUseCase
import com.pixnpunk.utils.SettingsUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RootExecutor(
    private val fillAndInsertEntryUseCase: FillAndInsertEntryUseCase
) : CoroutineExecutor<RootStore.Intent, RootStore.Action, RootStore.State, RootStore.Message, RootStore.Label>() {
    private val outScope = CoroutineScope(Dispatchers.Default)

    override fun executeIntent(intent: RootStore.Intent) {
        when (intent) {
            is RootStore.Intent.AddNewGame -> {
                outScope.launch { fillAndInsertEntryUseCase.execute(intent.unfilledEntry) }
            }
            is RootStore.Intent.DownloadNewGame -> {

            }
            is RootStore.Intent.StartSenderGameSetting -> {
                scope.launch {
                    SettingsUtil.settingsFlow.collect {
                        AddonsUtils.publishAppSettings(it)
                    }
                }
            }
        }
    }

    override fun executeAction(action: RootStore.Action) {
    }

    override fun dispose() {
        super.dispose()
        outScope.cancel()
    }
}