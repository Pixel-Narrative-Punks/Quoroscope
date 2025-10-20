package com.pixnpunk.addons.data.di

import com.pixnpunk.addons.data.repository.AddonsRepositoryImpl
import com.pixnpunk.addons.domain.repository.AddonsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AddonsRepositoryImpl) {
        bind<AddonsRepository>()
    }
}