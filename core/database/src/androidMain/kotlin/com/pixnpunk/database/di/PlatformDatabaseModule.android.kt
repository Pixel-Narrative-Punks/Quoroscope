package com.pixnpunk.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.anggrayudi.storage.file.DocumentFileCompat.fromFullPath
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.DB_FILE_NAME
import com.pixnpunk.database.dao.GameDao
import com.pixnpunk.utils.FileUtil.isWritableDir
import com.pixnpunk.utils.FileUtil.isWritableFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.contracts.ExperimentalContracts

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

@OptIn(ExperimentalContracts::class)
val updatePathCallback: RoomDatabase.Callback = object : RoomDatabase.Callback(), KoinComponent {
    override fun onOpen(connection: SQLiteConnection) {
        CoroutineScope(Dispatchers.IO).launch {
            val gameDao: GameDao by inject()
            gameDao.getAll().forEach { game ->
                if (game.gameDirPath.startsWith("/")) {
                    val gameDir = fromFullPath(getKoin().get(), game.gameDirPath)
                    if (gameDir.isWritableDir()) {
                        gameDao.updateGameDirPath(game.id, gameDir.uri.toString())
                    }
                }
                if (game.gameFilesPath.isNotEmpty()) {
                    val newGameFilesPath = mutableListOf<String>()
                    game.gameFilesPath.forEach {
                        if (it.startsWith("/")) {
                            val gameFile = fromFullPath(getKoin().get(), it)
                            if (gameFile.isWritableFile()) {
                                newGameFilesPath.add(gameFile.uri.toString())
                            }
                        }
                    }
                    if (newGameFilesPath.isNotEmpty()) {
                        gameDao.updateGameFilesPath(game.id, newGameFilesPath)
                    }
                }
            }
        }
    }
}