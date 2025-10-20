package com.pixnpunk.utils

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

actual object FileUtil {
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
}