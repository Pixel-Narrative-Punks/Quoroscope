package com.pixnpunk.utils

import com.pixnpunk.dto.SettingsEntity

expect object AddonsUtils {
    fun publishAppSettings(settings: SettingsEntity)
    fun receiveEngineAddons(): Boolean
}