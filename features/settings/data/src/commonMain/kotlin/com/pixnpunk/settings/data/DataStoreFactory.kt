package com.pixnpunk.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

expect fun getDatastoreFilePath(fileName: String): String

fun createDataStore(fileName: String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { getDatastoreFilePath(fileName).toPath() }
    )
}
