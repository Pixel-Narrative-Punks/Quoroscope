package com.pixnpunk.quoroscope

import android.app.Application
import com.pixnpunk.database.di.databaseModule
import com.pixnpunk.network.networkModule
import com.pixnpunk.addons.pluginsModule
import com.pixnpunk.addons.presentation.di.featureAddonsModule
import com.pixnpunk.stock.presentation.di.featureStockModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Quoroscope : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Quoroscope)
            modules(databaseModule, pluginsModule, networkModule)
            modules(featureStockModules)
            modules(featureAddonsModule)
        }
    }
}