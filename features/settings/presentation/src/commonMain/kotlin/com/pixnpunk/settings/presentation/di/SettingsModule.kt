package com.pixnpunk.settings.presentation.di

import com.pixnpunk.settings.data.di.settingsDataModule
import com.pixnpunk.settings.domain.di.settingsDomainModule

val featureSettingsModule = listOf(
    settingsDataModule,
    settingsDomainModule
)