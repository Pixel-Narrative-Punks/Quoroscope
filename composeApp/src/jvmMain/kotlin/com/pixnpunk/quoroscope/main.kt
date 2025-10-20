package com.pixnpunk.quoroscope

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.pixnpunk.database.di.databaseModule
import com.pixnpunk.network.networkModule
import com.pixnpunk.addons.pluginsModule
import com.pixnpunk.stock.presentation.di.featureStockModules
import org.koin.core.context.startKoin
import javax.swing.SwingUtilities

fun main() {
    startKoin {
        modules(databaseModule, pluginsModule, networkModule)
        modules(featureStockModules)
    }

    val root = runOnUiThread {
        RealRootComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            launchDirPicker = {
//            dirPickerLauncher.launch(null)
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