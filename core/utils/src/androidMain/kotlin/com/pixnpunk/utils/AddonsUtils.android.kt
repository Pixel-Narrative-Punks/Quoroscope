package com.pixnpunk.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.anggrayudi.storage.file.DocumentFileCompat
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.dto.SettingsEntity
import com.pixnpunk.utils.FileUtil.isWritableDir
import com.pixnpunk.utils.FileUtil.isWritableFile
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.contracts.ExperimentalContracts

actual object AddonsUtils : KoinComponent {

    private const val ENGINE_PKG_NAME: String = "com.pixnpunk.qsplugin"
    private const val ENGINE_CLS_NAME: String = "com.pixnpunk.qsplugin.MainActivity"
    private const val ENGINE_SET_ACT_NAME: String = "com.pixnpunk.settings.ACTION_SETTINGS_UPDATE"

    @OptIn(ExperimentalContracts::class)
    fun createEngineIntent(
        entry: GameEntry,
        fileSelIndex: Int,
        settingsEntity: SettingsEntity
    ): Intent {
        val gameDir = DocumentFileCompat.fromFullPath(
            context = get(),
            fullPath = entry.gameDirPath,
            requiresWriteAccess = true
        )
        val gameFile = DocumentFileCompat.fromFullPath(
            context = get(),
            fullPath = entry.gameFilesPath[fileSelIndex],
            requiresWriteAccess = true
        )
        val gameSettingsString = Json.encodeToString(settingsEntity)

        return if (!gameDir.isWritableDir() || !gameFile.isWritableFile()) {
            Intent()
        } else {
            Intent().apply {
                component = ComponentName(ENGINE_PKG_NAME, ENGINE_CLS_NAME)
                putExtra("gameId", entry.id)
                putExtra("gameTitle", entry.title)
                putExtra("gameDirUri", gameDir.uri)
                putExtra("gameFileUri", gameFile.uri)
                putExtra("gameAppSettings", gameSettingsString)
            }
        }
    }

    actual fun publishAppSettings(settings: SettingsEntity) {
        get<Context>().sendOrderedBroadcast(
            Intent(ENGINE_SET_ACT_NAME).apply {
                putExtra("gameAppSettings", Json.encodeToString(settings))
            }, null
        )
    }

    actual fun receiveEngineAddons(): Boolean {
        val testIntent = Intent().apply {
            component = ComponentName(ENGINE_PKG_NAME, ENGINE_CLS_NAME)
        }
        return get<Context>().packageManager.resolveActivity(
            testIntent,
            PackageManager.MATCH_ALL
        ) != null
    }
}