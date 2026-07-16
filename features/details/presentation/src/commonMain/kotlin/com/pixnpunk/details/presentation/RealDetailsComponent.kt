package com.pixnpunk.details.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.pixnpunk.details.presentation.mvi.DetailsStore
import com.pixnpunk.details.presentation.mvi.RealDetailsStore
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.dto.SettingsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import nl.jacobras.humanreadable.HumanReadable
import org.koin.core.component.KoinComponent
import kotlin.time.Instant

class RealDetailsComponent(
    private val componentContext: ComponentContext,
    private val defEntry: GameEntry,
    override val settingsState: StateFlow<SettingsEntity>,
    override val showSelectGameDialog: (GameEntry) -> Unit,
    override val showEditGameDialog: (GameEntry) -> Unit,
    override val showSettingsGamePanel: (GameEntry) -> Unit,
    override val showDownloadGameDialog: (GameEntry) -> Unit,
    override val onDismissed: () -> Unit
) : ComponentContext by componentContext, DetailsComponent, KoinComponent {

    private val store = instanceKeeper.getStore {
        RealDetailsStore(DefaultStoreFactory(), defEntry).create()
    }

    private val fallbackFormat = LocalDateTime.Format {
        year(); char('-'); monthNumber(); char('-'); dayOfMonth()
        char(' ')
        hour(); char(':'); minute(); char(':'); second()
    }

    private fun parseToInstant(dateStr: String): Instant? {
        if (dateStr.isEmpty()) return null
        return runCatching {
            Instant.parse(dateStr)
        }.recoverCatching {
            LocalDateTime.parse(dateStr, fallbackFormat).toInstant(TimeZone.UTC)
        }.getOrNull()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow
    override val label: Flow<DetailsStore.Label> = store.labels

    override fun convEntryData(
        entry: GameEntry
    ): GameEntry {
        if (entry.pubDate.isEmpty() || entry.modDate.isEmpty()) return entry
        return entry.copy(
            pubDate =
                parseToInstant(entry.pubDate)?.let { HumanReadable.timeAgo(it) } ?: entry.pubDate,
            modDate =
                parseToInstant(entry.modDate)?.let { HumanReadable.timeAgo(it) } ?: entry.modDate
        )
    }
}