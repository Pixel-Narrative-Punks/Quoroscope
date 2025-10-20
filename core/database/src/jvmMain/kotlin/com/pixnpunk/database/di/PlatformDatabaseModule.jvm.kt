package com.pixnpunk.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.DB_FILE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

internal actual fun platformDatabaseModule(): Module =
    module(createdAtStart = true) {
        single<AppDatabase> {
            Room.databaseBuilder<AppDatabase>(
                name = File(System.getProperty("java.io.tmpdir"), DB_FILE_NAME).absolutePath
            ).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
        }
    }
