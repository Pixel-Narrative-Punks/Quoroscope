package com.pixnpunk.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.pixnpunk.database.dao.GameDao
import com.pixnpunk.database.model.GameEntity

@Database(entities = [GameEntity::class], version = 1)
@TypeConverters(ListStringConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal const val DB_FILE_NAME = "game-entries.db"