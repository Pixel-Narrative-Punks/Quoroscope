package com.pixnpunk.quoroscope.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.pixnpunk.addons.AddonsUtils
import com.pixnpunk.stock.domain.usecase.DownloadGameArchUseCase
import com.pixnpunk.stock.domain.usecase.FillAndInsertEntryUseCase
import com.pixnpunk.stock.domain.usecase.UpsertEntryUseCase
import com.pixnpunk.utils.SettingsUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RootExecutor(
    private val fillAndInsertEntryUseCase: FillAndInsertEntryUseCase,
    private val upsertEntryUseCase: UpsertEntryUseCase,
    private val downloadGameArchUseCase: DownloadGameArchUseCase
) : CoroutineExecutor<RootStore.Intent, RootStore.Action, RootStore.State, RootStore.Message, RootStore.Label>() {
    private val outScope = CoroutineScope(Dispatchers.Default)

    override fun executeIntent(intent: RootStore.Intent) {
        when (intent) {
            is RootStore.Intent.AddNewGame -> {
                outScope.launch { fillAndInsertEntryUseCase.execute(intent.entry) }
            }

            is RootStore.Intent.EditGame -> {
                outScope.launch { upsertEntryUseCase.execute(intent.entry) }
            }

            is RootStore.Intent.DownloadNewGame -> {
                outScope.launch {
                    downloadGameArchUseCase.execute(
                        downloadLink = intent.entry.fileUrl,
                        destDirPath = intent.entry.gameDirPath
                    )
                }
            }

            is RootStore.Intent.UpdateSettingsColor -> {
                scope.launch {
                    SettingsUtil.emitColorValue(
                        intent.state, intent.color
                    )
                }
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