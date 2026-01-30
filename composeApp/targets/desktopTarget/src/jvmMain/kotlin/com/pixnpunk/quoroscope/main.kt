package com.pixnpunk.quoroscope

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.pixnpunk.addons.presentation.di.featureAddonsModule
import com.pixnpunk.database.di.databaseModule
import com.pixnpunk.network.networkModule
import com.pixnpunk.stock.presentation.di.featureStockModules
import com.pixnpunk.utils.settingsModule
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import javax.swing.SwingUtilities

private val scope = CoroutineScope(Dispatchers.Default)
private lateinit var root: RealRootComponent

fun main() {
    val application = startKoin {
        modules(databaseModule, networkModule, settingsModule)
        modules(featureStockModules)
        modules(featureAddonsModule)
    }

    FileKit.init(appId = "Quoroscope")

    root = runOnUiThread {
        RealRootComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            launchDirPicker = {
                scope.launch {
                    val directory = FileKit.openDirectoryPicker()
                    val fileName = directory?.nameWithoutExtension
                    val filePath = directory?.path
                    if (fileName != null && filePath != null) {
                        runOnUiThread { root.processPickedDir(fileName, filePath) }
                    }
                }
            },
            launchFilesPicker = {
                scope.launch {
                    val files = FileKit.openFilePicker(
                        mode = FileKitMode.Multiple(),
                        type = FileKitType.File("qsp", "gam")
                    )
                    val filePaths = files?.map { f -> f.path }
                    if (filePaths != null) {
                        runOnUiThread { root.processPickedFiles(filePaths) }
                    }
                }
            },
            launchFilePicker = {
                scope.launch {
                    val file = FileKit.openFilePicker(FileKitType.Image)
                    val filePath = file?.path
                    if (filePath != null) {
                        runOnUiThread { root.processPickedFile(filePath) }
                    }
                }
            },
            launchStorageAccess = {},
            launchEngineIntent = {

            }
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Quoroscope",
        ) {
            RootContent(root, application.koin.get())
        }
    }
}

internal fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }

    var error: Throwable? = null
    var result: T? = null

    SwingUtilities.invokeAndWait {
        try {
            result = block()
        } catch (e: Throwable) {
            error = e
        }
    }

    error?.also { throw it }

    @Suppress("UNCHECKED_CAST")
    return result as T
}