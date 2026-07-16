package com.pixnpunk.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.InputState
import com.pixnpunk.dto.SettingsEntity
import com.pixnpunk.settings.domain.model.PrefKeys
import com.pixnpunk.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    globalDataStore: DataStore<Preferences>,
    localDataStore: DataStore<Preferences>?
) : SettingsRepository {

    private val defValue = SettingsEntity()
    private val targetDataStore = localDataStore ?: globalDataStore

    override val settingsFlow: Flow<SettingsEntity> =
        if (localDataStore != null) {
            combine(
                globalDataStore.data,
                localDataStore.data
            ) { global, local ->
                mapToEntity(global, local)
            }
        } else {
            globalDataStore.data.map { global ->
                mapToEntity(global, null)
            }
        }

    private fun getIntMerged(
        global: Preferences,
        local: Preferences?,
        keyStr: String,
        default: Int
    ): Int {
        val key = intPreferencesKey(keyStr)
        return if (local != null && local.contains(key)) {
            local[key] ?: default
        } else {
            global[key] ?: default
        }
    }

    private fun getBooleanMerged(
        global: Preferences,
        local: Preferences?,
        keyStr: String,
        default: Boolean
    ): Boolean {
        val key = booleanPreferencesKey(keyStr)
        return if (local != null && local.contains(key)) {
            local[key] ?: default
        } else {
            global[key] ?: default
        }
    }

    private fun getStringMerged(
        global: Preferences,
        local: Preferences?,
        keyStr: String,
        default: String
    ): String {
        val key = stringPreferencesKey(keyStr)
        return if (local != null && local.contains(key)) {
            local[key] ?: default
        } else {
            global[key] ?: default
        }
    }

    private inline fun <reified T : Enum<T>> parseEnumSafety(value: String, default: T): T =
        enumValues<T>().find { it.name == value } ?: default

    private fun mapToEntity(global: Preferences, local: Preferences?) = SettingsEntity(
        fontSize = getIntMerged(global, local, PrefKeys.FONT_SIZE, defValue.fontSize),

        isUseCustomFont = getBooleanMerged(
            global,
            local,
            PrefKeys.CUSTOM_FONT_ENABLED,
            defValue.isUseCustomFont
        ),
        customFontPath = getStringMerged(
            global,
            local,
            PrefKeys.CUSTOM_FONT_PATH,
            defValue.customFontPath
        ),

        isTextColorOverride = getBooleanMerged(
            global,
            local,
            PrefKeys.TEXT_COLOR_OVERRIDE,
            defValue.isTextColorOverride
        ),
        textColor = getIntMerged(
            global,
            local,
            PrefKeys.TEXT_COLOR,
            defValue.textColor
        ),

        isBackColorOverride = getBooleanMerged(
            global,
            local,
            PrefKeys.BACK_COLOR_OVERRIDE,
            defValue.isBackColorOverride
        ),
        backColor = getIntMerged(
            global,
            local,
            PrefKeys.BACK_COLOR,
            defValue.backColor
        ),

        isLinkColorOverride = getBooleanMerged(
            global,
            local,
            PrefKeys.LINK_COLOR_OVERRIDE,
            defValue.isLinkColorOverride
        ),
        linkColor = getIntMerged(
            global,
            local,
            PrefKeys.LINK_COLOR,
            defValue.linkColor
        ),

        isImgLoadDis = getBooleanMerged(
            global,
            local,
            PrefKeys.IMG_LOAD_DISABLE,
            defValue.isImgLoadDis
        ),

        isAutoWidthImage = getBooleanMerged(
            global,
            local,
            PrefKeys.IMG_AUTO_WIDTH,
            defValue.isAutoWidthImage
        ),
        imageWidth = getIntMerged(
            global,
            local,
            PrefKeys.IMG_MANUAL_WIDTH,
            defValue.imageWidth
        ),

        isAutoHeightImage = getBooleanMerged(
            global,
            local,
            PrefKeys.IMG_AUTO_HEIGHT,
            defValue.isAutoHeightImage
        ),
        imageHeight = getIntMerged(
            global,
            local,
            PrefKeys.IMG_MANUAL_HEIGHT,
            defValue.imageHeight
        ),

        isUseFullscreenImages = getBooleanMerged(
            global,
            local,
            PrefKeys.IMG_IMMERSIVE,
            defValue.isUseFullscreenImages
        ),

        isUseImageDebug = getBooleanMerged(
            global,
            local,
            PrefKeys.IMG_DEBUG,
            defValue.isUseImageDebug
        ),

        isSoundEnabled = getBooleanMerged(
            global,
            local,
            PrefKeys.AUDIO_ENABLED,
            defValue.isSoundEnabled
        ),
        isVideoMute = getBooleanMerged(
            global,
            local,
            PrefKeys.VIDEO_MUTE,
            defValue.isVideoMute
        ),
        isUseMusicDebug = getBooleanMerged(
            global,
            local,
            PrefKeys.AUDIO_DEBUG,
            defValue.isUseMusicDebug
        ),

        isUseExecString = getBooleanMerged(
            global,
            local,
            PrefKeys.EXEC_STRING,
            defValue.isUseExecString
        ),
        isImmersModeEnabled = getBooleanMerged(
            global,
            local,
            PrefKeys.IMMERSIVE_MODE,
            defValue.isImmersModeEnabled
        ),

        visActsCount = getIntMerged(
            global,
            local,
            PrefKeys.ACTS_COUNT,
            defValue.visActsCount
        ),
        language = getStringMerged(
            global,
            local,
            PrefKeys.APP_LANG,
            defValue.language
        ),

        typeface = parseEnumSafety(
            getStringMerged(
                global,
                local,
                PrefKeys.FONT_STYLE,
                defValue.typeface.name
            ),
            defValue.typeface
        ),

        nativeLibFrom = parseEnumSafety(
            getStringMerged(
                global,
                local,
                PrefKeys.LIB_VERSION,
                defValue.nativeLibFrom.name
            ),
            defValue.nativeLibFrom
        ),

        appThemeMode = parseEnumSafety(
            getStringMerged(
                global,
                local,
                PrefKeys.APP_THEME,
                defValue.appThemeMode.name
            ),
            defValue.appThemeMode
        ),

        binaryPref = parseEnumSafety(
            getStringMerged(
                global,
                local,
                PrefKeys.BINARY_PREF,
                defValue.binaryPref.name
            ),
            defValue.binaryPref
        ),
    )

    override suspend fun updateColorValue(state: ColorPickerState, colorArgb: Int) {
        targetDataStore.edit { prefs ->
            when (state) {
                ColorPickerState.COLOR_TEXT ->
                    prefs[intPreferencesKey(PrefKeys.TEXT_COLOR)] = colorArgb

                ColorPickerState.COLOR_BACK ->
                    prefs[intPreferencesKey(PrefKeys.BACK_COLOR)] = colorArgb

                ColorPickerState.COLOR_LINK ->
                    prefs[intPreferencesKey(PrefKeys.LINK_COLOR)] = colorArgb
            }
        }
    }

    override suspend fun restoreColorValue(state: ColorPickerState) =
        updateColorValue(
            state, when (state) {
                ColorPickerState.COLOR_TEXT -> defValue.textColor
                ColorPickerState.COLOR_BACK -> defValue.backColor
                ColorPickerState.COLOR_LINK -> defValue.linkColor
            }
        )

    override suspend fun updateImageSize(state: InputState, value: Int) {
        targetDataStore.edit { prefs ->
            when (state) {
                InputState.INPUT_IMAGE_WIDTH ->
                    prefs[intPreferencesKey(PrefKeys.IMG_MANUAL_WIDTH)] = value

                InputState.INPUT_IMAGE_HEIGHT ->
                    prefs[intPreferencesKey(PrefKeys.IMG_MANUAL_HEIGHT)] = value
            }
        }
    }

    override suspend fun updateCustomFontPath(filePath: String) {
        targetDataStore.edit { prefs ->
            prefs[stringPreferencesKey(PrefKeys.CUSTOM_FONT_PATH)] = filePath
        }
    }
}
