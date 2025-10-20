package com.pixnpunk.utils

expect object FileUtil {
    fun formatFileSize(size: Long, numCountInfo: Int): String
    suspend fun createSpecialFiles(path: String)
}