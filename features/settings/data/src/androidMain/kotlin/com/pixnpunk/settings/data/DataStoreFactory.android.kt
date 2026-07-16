package com.pixnpunk.settings.data

import android.content.Context
import org.koin.java.KoinJavaComponent.get

actual fun getDatastoreFilePath(fileName: String): String {
    val context: Context = get(Context::class.java)
    return context.filesDir.resolve("datastore/$fileName").absolutePath
}
