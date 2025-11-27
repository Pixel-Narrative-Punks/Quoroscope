package com.pixnpunk.utils

import com.pixnpunk.dto.DownloadStatus
import com.pixnpunk.dto.ProgressState
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.FlowCollector
import net.sf.sevenzipjbinding.ExtractAskMode
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.IArchiveExtractCallback
import net.sf.sevenzipjbinding.IInArchive
import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.PropID
import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.text.DecimalFormat
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
import kotlin.math.log10
import kotlin.math.pow

actual object FileUtil {
    actual fun String.receiveRelPath(): String =
        File(this).let {
            return@let if (it.exists()) {
                it.parentFile.name + File.separator + it.nameWithoutExtension
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

    actual suspend fun createSpecialFiles(path: String) {
        // do nothing
    }

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
        val destFile = File(destDirPath, fileName)
        fileChannel.copyAndClose(destFile.writeChannel())
        progress.emit(
            ProgressState(
                progress = 1.0F,
                status = DownloadStatus.WRITE_FILE,
                time = ""
            )
        )
        return Result.success(destFile.absolutePath)
    }

    actual suspend fun unpackFileToDir(
        filePath: String, // abs path
        destDirPath: String, // abs path
        progress: FlowCollector<ProgressState>
    ) =
        runCatching {
            RandomAccessFileInStream(RandomAccessFile(File(filePath), "r")).use {
                SevenZip.openInArchive(null, it).use { inArchive ->
                    inArchive.extract(
                        null,
                        false,
                        ArchiveExtractCallback(
                            Path(destDirPath),
                            inArchive
                        )
                    )
                }
            }
            Path(filePath).deleteIfExists()
            Path(destDirPath).toFile().listFiles().first { f -> f.isDirectory }.absolutePath
        }

    private class ArchiveExtractCallback(
        val targetFolder: Path,
        val inArchive: IInArchive
    ) : IArchiveExtractCallback {
        private var extractAskMode: ExtractAskMode? = null
        private var stream: SequentialOutStream? = null

        @Throws(SevenZipException::class)
        override fun getStream(index: Int, extractAskMode: ExtractAskMode?): ISequentialOutStream? {
            this.extractAskMode = extractAskMode

            val isFolder = inArchive.getProperty(index, PropID.IS_FOLDER) as? Boolean ?: false
            val pathString  = inArchive.getProperty(index, PropID.PATH) as? String ?: return null

            val destinationPath = targetFolder.resolve(pathString).normalize()

            if (isFolder) {
                try {
                    Files.createDirectories(destinationPath)
                } catch (e: IOException) {
                    println("Failed to create directory: $destinationPath | $e")
                }
                return null
            }

            val parentDir = destinationPath.parent
            if (parentDir != null) {
                try {
                    Files.createDirectories(parentDir)
                } catch (e: IOException) {
                    println("Failed to create directory: $destinationPath | $e")
                    return null
                }
            }

            if (destinationPath.isDirectory()) {
                println("Cannot overwrite a directory with a file: $destinationPath")
                return null
            }

            return try {
                SequentialOutStream(Files.newOutputStream(destinationPath))
            } catch (e: IOException) {
                println("Error creating output stream for: $destinationPath | $e")
                null
            }
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