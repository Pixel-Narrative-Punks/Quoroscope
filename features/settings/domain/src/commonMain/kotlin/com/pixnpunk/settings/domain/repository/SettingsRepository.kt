package com.pixnpunk.settings.domain.repository

import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.InputState
import com.pixnpunk.dto.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<SettingsEntity>
    suspend fun updateColorValue(state: ColorPickerState, colorArgb: Int)
    suspend fun restoreColorValue(state: ColorPickerState)
    suspend fun updateImageSize(state: InputState, value: Int)
    suspend fun updateCustomFontPath(filePath: String)
}
