package com.pixnpunk.utils

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.DocumentFileCompat.fromUri
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.children
import com.anggrayudi.storage.file.getRelativePath
import com.anggrayudi.storage.file.isWritable
import com.anggrayudi.storage.file.makeFile
import com.anggrayudi.storage.file.makeFolder
import com.anggrayudi.storage.file.openOutputStream
import com.pixnpunk.dto.DownloadStatus
import com.pixnpunk.dto.ProgressState
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.flow.FlowCollector
import net.sf.sevenzipjbinding.ExtractAskMode
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.IArchiveExtractCallback
import net.sf.sevenzipjbinding.IInArchive
import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.PropID
import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.SevenZipException
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
import java.text.DecimalFormat
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.io.path.isDirectory
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

    actual fun String.receiveRelPath() =
        fromUri(get(), this.toUri())?.getRelativePath(get()) ?: ""

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

    @OptIn(ExperimentalContracts::class)
    actual suspend fun unpackFileToDir(
        filePath: String,
        destDirPath: String,
        progress: FlowCollector<ProgressState>
    ) =
        runCatching {
            val dir = fromUri(get(), destDirPath.toUri())
            if (!dir.isWritableDir()) throw FileNotFoundException()
            val file = fromUri(get(), filePath.toUri())
            if (!file.isWritableFile()) throw FileNotFoundException()
            val unpackDir = dir.makeFolder(get(), "CG${file.name}")
            if (!unpackDir.isWritableDir()) throw FileNotFoundException()
            DocumentFileRandomInStream(file.uri).use {
                SevenZip.openInArchive(null, it).use { inArchive ->
                    inArchive.extract(
                        null,
                        false,
                        ArchiveExtractCallback(unpackDir, inArchive)
                    )
                }
            }
            file.delete()
            return@runCatching unpackDir.uri.toString()
        }

    private class ArchiveExtractCallback(
        val targetFolder: DocumentFile,
        val inArchive: IInArchive
    ) : IArchiveExtractCallback, KoinComponent {
        private var extractAskMode: ExtractAskMode? = null
        private var stream: SequentialOutStream? = null

        @Throws(SevenZipException::class)
        override fun getStream(index: Int, extractAskMode: ExtractAskMode?): ISequentialOutStream? {
            this.extractAskMode = extractAskMode

            val isFolder = inArchive.getProperty(index, PropID.IS_FOLDER) as? Boolean ?: false
            val pathString  = inArchive.getProperty(index, PropID.PATH) as? String ?: return null

            if (isFolder) {
                try {
                    targetFolder.makeFolder(get(), pathString)
                } catch (e: IOException) {
                    println("Failed to create directory: $pathString | $e")
                }
                return null
            }

            val file =
                targetFolder.makeFile(get(), pathString, mode = CreateMode.REUSE) ?: return null
            if (file.isDirectory) return null
            try {
                stream = SequentialOutStream(
                    get<Context>().contentResolver.openOutputStream(file.uri)!!
                )
            } catch (e: Exception) {
                println("Error: $e")
            }
            return stream
        }

        override fun prepareOperation(askMode: ExtractAskMode?) {
            println("Extract archive, prepare to: $askMode")
        }

        @Throws(SevenZipException::class)
        override fun setOperationResult(extractOperationResult: ExtractOperationResult) {
            try {
                stream?.close()
                stream = null
            } catch (e: IOException) {
                throw SevenZipException(e)
            }

            if (extractOperationResult != ExtractOperationResult.OK) {
                throw SevenZipException(extractOperationResult.toString())
            }
        }

        override fun setTotal(total: Long) {
            println("Extract archive, work planned: $total")
        }

        override fun setCompleted(complete: Long) {
            println("Extract archive, work completed: $complete")
        }
    }

    private class SequentialOutStream(
        val out: OutputStream
    ) : ISequentialOutStream {
        @Throws(SevenZipException::class)
        override fun write(data: ByteArray?): Int {
            if (data == null || data.isEmpty()) {
                throw SevenZipException("null data")
            }
            try {
                out.write(data)
            } catch (e: IOException) {
                throw SevenZipException(e)
            }
            return data.size
        }

        @Throws(IOException::class)
        fun close() {
            out.close()
        }
    }
}