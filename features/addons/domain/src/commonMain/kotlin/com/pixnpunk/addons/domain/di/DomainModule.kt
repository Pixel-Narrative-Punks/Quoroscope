package com.pixnpunk.addons.domain.di

import com.pixnpunk.addons.domain.usecase.GetAllAddonsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetAllAddonsUseCase)
}