package com.pixnpunk.utils

import io.ktor.utils.io.ByteReadChannel

expect object FileUtil {
    fun String.receiveRelPath(): String
    fun formatFileSize(size: Long, numCountInfo: Int): String
    suspend fun createSpecialFiles(path: String)
    suspend fun writeFileToDir(fileChannel: ByteReadChannel, fileName: String, destDirPath: String)
}