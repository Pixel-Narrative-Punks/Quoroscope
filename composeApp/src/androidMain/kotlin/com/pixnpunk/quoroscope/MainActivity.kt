package com.pixnpunk.quoroscope

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.anggrayudi.storage.SimpleStorage.Companion.cleanupRedundantUriPermissions
import com.anggrayudi.storage.SimpleStorage.Companion.hasFullDiskAccess
import com.anggrayudi.storage.contract.FolderPickerResult
import com.anggrayudi.storage.contract.OpenFolderPickerContract
import com.anggrayudi.storage.contract.RequestStorageAccessContract
import com.anggrayudi.storage.contract.RequestStorageAccessResult
import com.anggrayudi.storage.extension.startActivitySafely
import com.anggrayudi.storage.extension.toDocumentFile
import com.anggrayudi.storage.file.FileFullPath
import com.anggrayudi.storage.file.MimeType
import com.anggrayudi.storage.file.StorageId
import com.anggrayudi.storage.file.getAbsolutePath
import com.arkivanov.decompose.defaultComponentContext
import com.pixnpunk.utils.AddonsUtils
import com.pixnpunk.utils.FileUtil.isWritableDir
import com.pixnpunk.utils.FileUtil.isWritableFile
import com.pixnpunk.utils.SettingsRepository
import org.koin.android.ext.android.get
import kotlin.concurrent.thread
import kotlin.contracts.ExperimentalContracts

class MainActivity : ComponentActivity() {

    private val settingsRepository: SettingsRepository = get()

    private lateinit var root: RealRootComponent

    @OptIn(ExperimentalContracts::class)
    private val dirPickerLauncher =
        registerForActivityResult(OpenFolderPickerContract(get())) {
            println(it.toString())
            when (it) {
                is FolderPickerResult.AccessDenied -> {
                    val fileAbsPath = it.folder?.getAbsolutePath(this)
                    if (fileAbsPath != null) {
                        storageAccessLauncher.launch(
                            RequestStorageAccessContract.Options(
                                FileFullPath(this, fileAbsPath)
                            )
                        )
                    }
                }

                is FolderPickerResult.CanceledByUser -> {
                }

                is FolderPickerResult.Picked -> {
                    var newGameDir = it.folder
                    if (newGameDir.isWritableDir()) {
                        val path = newGameDir.getAbsolutePath(this)
                        val dirName = newGameDir.name

                        if (path.isNotEmpty() && dirName != null) {
                            root.processPickedDir(dirName, path)
                        }
                    }
                }
            }
        }

    @OptIn(ExperimentalContracts::class)
    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            if (it != null) {
                val contentResolver = applicationContext.contentResolver
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                thread { cleanupRedundantUriPermissions(applicationContext) }

                var newGameDir = it.toDocumentFile(this)
                if (newGameDir.isWritableFile()) {
                    val path = newGameDir.getAbsolutePath(this)
                    if (path.isNotEmpty()) {
                        root.processPickedFile(path)
                    }
                }
            }
        }

    private val storageAccessLauncher =
        registerForActivityResult(RequestStorageAccessContract(get())) { result ->
            when (result) {
                is RequestStorageAccessResult.CanceledByUser -> {

                }

                is RequestStorageAccessResult.ExpectedStorageNotSelected -> {

                }

                is RequestStorageAccessResult.RootPathNotSelected -> {

                }

                is RequestStorageAccessResult.RootPathPermissionGranted -> {

                }

                is RequestStorageAccessResult.StoragePermissionDenied -> {

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (!hasFullDiskAccess(this, StorageId.PRIMARY)) {
                storageAccessLauncher.launch(RequestStorageAccessContract.Options())
            }
        }

        root = RealRootComponent(
            componentContext = defaultComponentContext(),
            launchDirPicker = {
                dirPickerLauncher.launch(OpenFolderPickerContract.Options())
            },
            launchFilePicker = {
                filePickerLauncher.launch(arrayOf(MimeType.IMAGE))
            },
            launchEngineIntent = { (entry, i) ->
                startActivitySafely(
                    AddonsUtils.createEngineIntent(
                        entry = entry,
                        fileSelIndex = i,
                        settingsEntity = settingsRepository.settingsFlow.value
                    )
                )
            }
        )

        setContent { RootContent(root, settingsRepository) }
    }
}
