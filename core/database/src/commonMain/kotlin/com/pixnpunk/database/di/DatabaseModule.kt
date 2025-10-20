package com.pixnpunk.database.di

import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.dao.GameDao

val databaseModule
    get() = platformDatabaseModule().apply {
        single<GameDao> { get<AppDatabase>().gameDao() }
    }