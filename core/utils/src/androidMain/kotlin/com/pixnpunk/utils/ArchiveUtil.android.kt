package com.pixnpunk.utils

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.anggrayudi.storage.file.CreateMode
import com.anggrayudi.storage.file.DocumentFileCompat.fromFullPath
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.makeFile
import com.anggrayudi.storage.file.makeFolder
import com.pixnpunk.utils.FileUtil.isWritableDir
import net.sf.sevenzipjbinding.ExtractAskMode
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.IArchiveExtractCallback
import net.sf.sevenzipjbinding.IInArchive
import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.PropID
import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.io.RandomAccessFile
import kotlin.contracts.ExperimentalContracts

actual object ArchiveUtil : KoinComponent {
    @OptIn(ExperimentalContracts::class)
    actual suspend fun extractArchiveEntries(targetArchive: File, destDir: String) {
        val gameDir = fromFullPath(
            context = get(),
            fullPath = destDir,
            requiresWriteAccess = true
        )
        if (!gameDir.isWritableDir()) return

        RandomAccessFile(targetArchive, "r").use { raf ->
            val inStream = RandomAccessFileInStream(raf)
            SevenZip.openInArchive(null, inStream).use { inArchive ->
                inArchive.extract(
                    null, false, ArchiveExtractCallback(gameDir, inArchive)
                )
            }
        }
    }

    private class ArchiveExtractCallback(
        private val destDir: DocumentFile,
        private val inArchive: IInArchive
    ) : IArchiveExtractCallback {
        private var extractAskMode: ExtractAskMode? = null
        private var stream: SequentialOutStream? = null

        @Throws(SevenZipException::class)
        override fun getStream(index: Int, extractAskMode: ExtractAskMode?): ISequentialOutStream? {
            this.extractAskMode = extractAskMode

            val isFolder = inArchive.getProperty(index, PropID.IS_FOLDER) as Boolean
            val path = inArchive.getProperty(index, PropID.PATH) as? String? ?: ""

            if (isFolder) {
                destDir.makeFolder(get<Context>(), path, CreateMode.REUSE)
                return null
            }

            val file =
                destDir.makeFile(get<Context>(), path, MimeType.UNKNOWN, CreateMode.REUSE)
            if (file == null) return null

            try {
                stream = SequentialOutStream(
                    get<Context>().contentResolver.openOutputStream(file.uri)!!
                )
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Error: ", e)
            }
            return stream
        }

        override fun prepareOperation(extractAskMode: ExtractAskMode?) {
        }

        @Throws(SevenZipException::class)
        override fun setOperationResult(extractOperationResult: ExtractOperationResult) {
            try {
                if (stream != null) stream!!.close()
                stream = null
            } catch (e: IOException) {
                throw SevenZipException(e)
            }

            if (extractOperationResult != ExtractOperationResult.OK) {
                throw SevenZipException(extractOperationResult.toString())
            }
        }

        override fun setTotal(total: Long) {
        }

        override fun setCompleted(complete: Long) {
        }
    }

    private class SequentialOutStream(private val out: OutputStream) : ISequentialOutStream {
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