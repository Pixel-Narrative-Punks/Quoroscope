package com.pixnpunk.addons

import org.koin.dsl.module

val pluginsModule = module {
    single { AddonsUtils }
}