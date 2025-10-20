package com.pixnpunk.addons.presentation.di

import com.pixnpunk.addons.data.di.dataModule
import com.pixnpunk.addons.domain.di.domainModule

val featureAddonsModule = listOf(
    dataModule,
    domainModule
)