package com.pixnpunk.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.pixnpunk.database.dao.AuthorDao
import com.pixnpunk.database.dao.GameDao
import com.pixnpunk.database.dao.SaveSlotDao
import com.pixnpunk.database.dao.TranslatorDao
import com.pixnpunk.database.model.AuthorEntity
import com.pixnpunk.database.model.GameEntity
import com.pixnpunk.database.model.GameFTS
import com.pixnpunk.database.model.SaveSlotEntity
import com.pixnpunk.database.model.TranslatorEntity

@Database(
    entities = [
        GameEntity::class,
        SaveSlotEntity::class,
        GameFTS::class,
        AuthorEntity::class,
        TranslatorEntity::class
    ],
    version = 3
)
@TypeConverters(ListStringConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun authorDao(): AuthorDao
    abstract fun translatorDao(): TranslatorDao
    abstract fun saveSlotDao(): SaveSlotDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal const val DB_FILE_NAME = "game-entries.db"