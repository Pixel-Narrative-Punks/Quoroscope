package com.pixnpunk.database.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.DB_FILE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformDatabaseModule(): Module =
    module(createdAtStart = true) {
        single<AppDatabase> {
            val context = get<Context>()
            Room.databaseBuilder<AppDatabase>(
                context = context,
                name = context.getDatabasePath(DB_FILE_NAME).absolutePath
            ).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
        }
    }
