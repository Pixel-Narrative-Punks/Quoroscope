package com.pixnpunk.addons

import com.pixnpunk.dto.GameSettings

expect object AddonsUtils {
    fun publishAppSettings(settings: GameSettings)
    fun receiveEngineAddons() : Boolean
}