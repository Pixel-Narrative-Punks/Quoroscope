package com.pixnpunk.utils

import java.io.File

expect object ArchiveUtil {
    suspend fun extractArchiveEntries(targetArchive: File, destDir: String)
}