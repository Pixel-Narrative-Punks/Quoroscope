package com.pixnpunk.settings.data

import java.io.File

actual fun getDatastoreFilePath(fileName: String): String {
    val appDir = File(System.getProperty("user.home"), ".quoroscope")
    if (!appDir.exists()) appDir.mkdirs()
    return File(appDir, fileName).absolutePath
}
