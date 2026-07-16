package com.pixnpunk.utils

import com.pixnpunk.dto.BinaryPrefFormat
import com.pixnpunk.dto.ProgressState
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.flow.FlowCollector

expect object FileUtil {
    fun String.receiveBaseName(): String
    fun String.receiveFullName(): String
    fun String.receiveDirRelPath(): String
    fun String.receiveFileRelPath(): String

    fun formatFileSize(size: Long, prefFormat: BinaryPrefFormat): String
    suspend fun createSpecialFiles(path: String)
    suspend fun writeFileToDir(
        fileChannel: ByteReadChannel,
        fileName: String,
        destDirPath: String,
        progress: FlowCollector<ProgressState>
    ): Result<String>
    suspend fun unpackFileToDir(
        filePath: String,
        destDirPath: String,
        progress: FlowCollector<ProgressState>
    ): Result<Unit>
}