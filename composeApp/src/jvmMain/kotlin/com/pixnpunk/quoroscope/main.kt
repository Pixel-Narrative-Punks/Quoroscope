package com.pixnpunk.quoroscope

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.pixnpunk.addons.pluginsModule
import com.pixnpunk.database.di.databaseModule
import com.pixnpunk.network.networkModule
import com.pixnpunk.stock.presentation.di.featureStockModules
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
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
    startKoin {
        modules(databaseModule, pluginsModule, networkModule)
        modules(featureStockModules)
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
                        root.showAddNewGameDialog(fileName, filePath)
                    }
                }
            },
            launchEngineIntent = {

            }
        )
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Quoroscope",
        ) {
            RootContent(root)
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