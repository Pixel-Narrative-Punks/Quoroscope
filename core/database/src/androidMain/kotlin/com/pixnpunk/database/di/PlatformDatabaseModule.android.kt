package com.pixnpunk.database.di

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.DB_FILE_NAME
import com.pixnpunk.database.dao.GameDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

internal actual fun platformDatabaseModule(): Module =
    module(createdAtStart = true) {
        single<AppDatabase> {
            Room.databaseBuilder<AppDatabase>(
                context = get<Context>(),
                name = get<Context>().getDatabasePath(DB_FILE_NAME).absolutePath
            )
                .addCallback(updatePathCallback)
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        }
    }

val updatePathCallback: RoomDatabase.Callback = object : RoomDatabase.Callback(), KoinComponent {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        CoroutineScope(Dispatchers.IO).launch {
            val gameDao: GameDao by inject()
            gameDao.getAll().forEach { game ->
                if (game.gameDirPath.startsWith("/")) {
                    val file = File(game.gameDirPath)
                    val newUriString = Uri.fromFile(file).toString()
                    gameDao.updateGamePath(game.id, newUriString)
                }
            }
        }
    }
}