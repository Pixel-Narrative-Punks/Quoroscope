package com.pixnpunk.utils

import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File
import java.text.DecimalFormat
import kotlin.io.path.Path
import kotlin.math.log10
import kotlin.math.pow

actual object FileUtil {
    actual fun String.receiveRelPath(): String =
        File(this).name

    actual fun formatFileSize(size: Long, numCountInfo: Int): String {
        if (size <= 0) {
            return "0"
        }
        var units = arrayOfNulls<String>(0)
        if (numCountInfo == 1000) {
            units = arrayOf("B", "KB", "MB", "GB", "TB")
        } else if (numCountInfo == 1024) {
            units = arrayOf("B", "KiB", "MiB", "GiB", "TiB")
        }
        val digitGroups = (log10(size.toDouble()) / log10(numCountInfo.toDouble())).toInt()
        return (DecimalFormat("#,##0.#").format(
            size / numCountInfo.toDouble().pow(digitGroups.toDouble())
        ) + " " + units[digitGroups])
    }

    actual suspend fun createSpecialFiles(path: String) {
        // do nothing
    }

    actual suspend fun writeFileToDir(
        fileChannel: ByteReadChannel,
        fileName: String,
        destDirPath: String
    ) {
        val destFile = File(destDirPath, fileName)
        fileChannel.copyAndClose(destFile.writeChannel())
    }
}