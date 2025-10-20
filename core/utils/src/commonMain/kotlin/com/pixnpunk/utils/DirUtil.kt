package com.pixnpunk.utils

expect object DirUtil {
    suspend fun receiveGameFilesFromDir(rootDirPath: String) : List<String>
    suspend fun tryReceiveDirSize(rootDirPath: String) : Long
}