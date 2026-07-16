package com.pixnpunk.settings.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pixnpunk.settings.data.createDataStore
import com.pixnpunk.settings.data.repository.SettingsRepositoryImpl
import com.pixnpunk.settings.domain.repository.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsDataModule = module {
    single(named("global_datastore")) {
        createDataStore("global_settings.preferences_pb")
    }

    factory<SettingsRepository> { params ->
        val gameId = params.getOrNull<Long>()?.takeIf { it != -1L }

        val globalDataStore: DataStore<Preferences> = get(named("global_datastore"))
        val localDataStore: DataStore<Preferences>? = if (gameId != null) {
            createDataStore("game_settings_${gameId}.preferences_pb")
        } else null

        SettingsRepositoryImpl(
            globalDataStore = globalDataStore,
            localDataStore = localDataStore
        )
    }
}
