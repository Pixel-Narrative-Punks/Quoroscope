package com.pixnpunk.quoroscope.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.pixnpunk.stock.domain.usecase.DownloadGameArchUseCase
import com.pixnpunk.stock.domain.usecase.FillAndInsertEntryUseCase
import com.pixnpunk.stock.domain.usecase.UpsertEntryUseCase
import com.pixnpunk.utils.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RootExecutor(
    private val fillAndInsertEntryUseCase: FillAndInsertEntryUseCase,
    private val upsertEntryUseCase: UpsertEntryUseCase,
    private val downloadGameArchUseCase: DownloadGameArchUseCase,
    private val settingsRepository: SettingsRepository
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
                    settingsRepository.emitColorValue(
                        intent.state, intent.color
                    )
                }
            }
        }
    }

    override fun dispose() {
        super.dispose()
        outScope.cancel()
    }
}