package com.pixnpunk.utils

import java.io.File

actual object DirUtil {
    actual suspend fun receiveGameFilesFromDir(rootDirPath: String): List<String> =
        File(rootDirPath)
            .walkTopDown()
            .filter { it.endsWith("qsp") || it.endsWith("gam") }
            .map { it.absolutePath }
            .toList()

    actual suspend fun tryReceiveDirSize(rootDirPath: String): Long =
        File(rootDirPath)
            .walkTopDown()
            .filter { it.isFile }
            .map { it.length() }
            .sum()
}