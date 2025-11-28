package com.pixnpunk.utils

import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.DocumentFileCompat.fromUri
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.baseName
import com.anggrayudi.storage.file.getRelativePath
import com.anggrayudi.storage.file.isWritable
import com.anggrayudi.storage.file.makeFile
import com.anggrayudi.storage.file.openOutputStream
import com.pixnpunk.dto.DownloadStatus
import com.pixnpunk.dto.ProgressState
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.flow.FlowCollector
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.text.DecimalFormat
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.log10
import kotlin.math.pow

actual object FileUtil : KoinComponent {
    @ExperimentalContracts
    fun DocumentFile?.isWritableFile(): Boolean {
        contract {
            returns() implies (this@isWritableFile != null)
        }
        if (this == null) return false
        return this.exists() && this.isFile && this.isWritable(get())
    }

    @ExperimentalContracts
    fun DocumentFile?.isWritableDir(): Boolean {
        contract {
            returns() implies (this@isWritableDir != null)
        }
        if (this == null) return false
        return this.exists() && this.isDirectory && this.isWritable(get())
    }

    actual fun String.receiveRelPath() = fromUri(get(), this.toUri()).let {
        return@let if (it != null) {
            it.getRelativePath(get()) + "/" + it.baseName
        } else { "" }
    }

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

    const val NOMEDIA_FILENAME: String = ".nomedia"
    const val NOSEARCH_FILENAME: String = ".nosearch"

    @OptIn(ExperimentalContracts::class)
    actual suspend fun createSpecialFiles(path: String) {
        val gameDir = fromUri(get(), path.toUri())
        if (!gameDir.isWritableDir()) return

        gameDir.makeFile(get(), NOMEDIA_FILENAME, MimeType.TEXT, CreateMode.SKIP_IF_EXISTS)
        gameDir.makeFile(get(), NOSEARCH_FILENAME, MimeType.TEXT, CreateMode.SKIP_IF_EXISTS)
    }

    @OptIn(ExperimentalContracts::class)
    actual suspend fun writeFileToDir(
        fileChannel: ByteReadChannel,
        fileName: String,
        destDirPath: String,
        progress: FlowCollector<ProgressState>
    ): Result<String> = runCatching {
        progress.emit(
            ProgressState(
                progress = 0F,
                status = DownloadStatus.WRITE_FILE,
                time = ""
            )
        )
        val gameDir = fromUri(get(), destDirPath.toUri())
        if (!gameDir.isWritableDir()) return Result.failure(NullPointerException())
        val destFile = gameDir.makeFile(get(), fileName, mode = CreateMode.SKIP_IF_EXISTS)
        if (!destFile.isWritableFile()) return Result.failure(NullPointerException())
        fileChannel.copyTo(destFile.openOutputStream(get())!!)
        progress.emit(
            ProgressState(
                progress = 1.0F,
                status = DownloadStatus.WRITE_FILE,
                time = ""
            )
        )
        return Result.success(destFile.uri.toString())
    }

    // TODO: Implement this function
    actual suspend fun unpackFileToDir(
        filePath: String,
        destDirPath: String,
        progress: FlowCollector<ProgressState>
    ) = runCatching { return@runCatching "" }
}