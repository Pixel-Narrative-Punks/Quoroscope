package com.pixnpunk.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.DB_FILE_NAME
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun platformDatabaseModule(): Module =
    module(createdAtStart = true) {
        single<AppDatabase> {
            Room.databaseBuilder<AppDatabase>(
                context = get<Context>(),
                name = get<Context>().getDatabasePath(DB_FILE_NAME).absolutePath
            ).addMigrations(MIGRATION_1_2)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE VIRTUAL TABLE IF NOT EXISTS `game_fts` 
            USING fts4(
                content=`game`, 
                `id`, 
                `listId`, 
                `author`, 
                `portedBy`, 
                `version`, 
                `title`, 
                `lang`, 
                `player`, 
                `gameIconPath`, 
                `fileUrl`, 
                `fileSize`, 
                `fileExt`, 
                `descUrl`, 
                `pubDate`, 
                `modDate`, 
                `gameDirPath`, 
                `gameFilesPath`
            )
        """
        )
    }
}
