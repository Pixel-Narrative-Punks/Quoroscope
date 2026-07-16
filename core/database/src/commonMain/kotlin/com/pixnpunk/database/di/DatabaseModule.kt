package com.pixnpunk.database.di

import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.dao.AuthorDao
import com.pixnpunk.database.dao.GameDao
import com.pixnpunk.database.dao.SaveSlotDao
import com.pixnpunk.database.dao.TranslatorDao

val databaseModule
    get() = platformDatabaseModule().apply {
        single<GameDao> { get<AppDatabase>().gameDao() }
        single<AuthorDao> { get<AppDatabase>().authorDao() }
        single<TranslatorDao> { get<AppDatabase>().translatorDao() }
        single<SaveSlotDao> { get<AppDatabase>().saveSlotDao() }
    }