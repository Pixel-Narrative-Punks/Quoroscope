package com.pixnpunk.database

import androidx.room.AutoMigration
import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.pixnpunk.database.dao.GameDao
import com.pixnpunk.database.model.GameEntity
import com.pixnpunk.database.model.GameFTS

@Database(
    entities = [GameEntity::class, GameFTS::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    version = 2)
@TypeConverters(ListStringConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal const val DB_FILE_NAME = "game-entries.db"