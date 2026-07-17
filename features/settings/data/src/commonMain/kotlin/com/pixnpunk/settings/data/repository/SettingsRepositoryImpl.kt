package com.pixnpunk.settings.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pixnpunk.dto.AppThemeMode
import com.pixnpunk.dto.BinaryPrefFormat
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.FontTypeface
import com.pixnpunk.dto.InputState
import com.pixnpunk.dto.NativeLibAuthors
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

    override suspend fun updateFontSize(fontSize: Int) {
        targetDataStore.edit { it[intPreferencesKey(PrefKeys.FONT_SIZE)] = fontSize }
    }

    override suspend fun updateFontStyle(typeface: FontTypeface) {
        targetDataStore.edit { it[stringPreferencesKey(PrefKeys.FONT_STYLE)] = typeface.name }
    }

    override suspend fun toggleCustomFont(isEnabled: Boolean, fontPath: String?) {
        targetDataStore.edit { prefs ->
            prefs[booleanPreferencesKey(PrefKeys.CUSTOM_FONT_ENABLED)] = isEnabled
            if (fontPath != null) {
                prefs[stringPreferencesKey(PrefKeys.CUSTOM_FONT_PATH)] = fontPath
            }
        }
    }

    override suspend fun toggleColorOverride(state: ColorPickerState, isOverride: Boolean) {
        targetDataStore.edit { prefs ->
            when (state) {
                ColorPickerState.COLOR_TEXT -> prefs[booleanPreferencesKey(PrefKeys.TEXT_COLOR_OVERRIDE)] = isOverride
                ColorPickerState.COLOR_BACK -> prefs[booleanPreferencesKey(PrefKeys.BACK_COLOR_OVERRIDE)] = isOverride
                ColorPickerState.COLOR_LINK -> prefs[booleanPreferencesKey(PrefKeys.LINK_COLOR_OVERRIDE)] = isOverride
            }
        }
    }

    override suspend fun updateColorValue(state: ColorPickerState, colorArgb: Int) {
        targetDataStore.edit { prefs ->
            when (state) {
                ColorPickerState.COLOR_TEXT -> prefs[intPreferencesKey(PrefKeys.TEXT_COLOR)] = colorArgb
                ColorPickerState.COLOR_BACK -> prefs[intPreferencesKey(PrefKeys.BACK_COLOR)] = colorArgb
                ColorPickerState.COLOR_LINK -> prefs[intPreferencesKey(PrefKeys.LINK_COLOR)] = colorArgb
            }
        }
    }

    override suspend fun restoreColorValue(state: ColorPickerState) {
        val defValue = SettingsEntity()
        val defaultColor = when (state) {
            ColorPickerState.COLOR_TEXT -> defValue.textColor
            ColorPickerState.COLOR_BACK -> defValue.backColor
            ColorPickerState.COLOR_LINK -> defValue.linkColor
        }
        updateColorValue(state, defaultColor)
    }

    override suspend fun toggleImageLoading(isDisabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.IMG_LOAD_DISABLE)] = isDisabled }
    }

    override suspend fun toggleAutoImageSize(state: InputState, isAuto: Boolean) {
        targetDataStore.edit { prefs ->
            when (state) {
                InputState.INPUT_IMAGE_WIDTH -> prefs[booleanPreferencesKey(PrefKeys.IMG_AUTO_WIDTH)] = isAuto
                InputState.INPUT_IMAGE_HEIGHT -> prefs[booleanPreferencesKey(PrefKeys.IMG_AUTO_HEIGHT)] = isAuto
            }
        }
    }

    override suspend fun updateImageSize(state: InputState, value: Int) {
        targetDataStore.edit { prefs ->
            when (state) {
                InputState.INPUT_IMAGE_WIDTH -> prefs[intPreferencesKey(PrefKeys.IMG_MANUAL_WIDTH)] = value
                InputState.INPUT_IMAGE_HEIGHT -> prefs[intPreferencesKey(PrefKeys.IMG_MANUAL_HEIGHT)] = value
            }
        }
    }

    override suspend fun toggleFullscreenImages(isImmersive: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.IMG_IMMERSIVE)] = isImmersive }
    }

    override suspend fun toggleImageDebug(isEnabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.IMG_DEBUG)] = isEnabled }
    }

    override suspend fun toggleSound(isEnabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.AUDIO_ENABLED)] = isEnabled }
    }

    override suspend fun toggleVideoMute(isMuted: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.VIDEO_MUTE)] = isMuted }
    }

    override suspend fun toggleAudioDebug(isEnabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.AUDIO_DEBUG)] = isEnabled }
    }

    override suspend fun updateLanguage(langCode: String) {
        targetDataStore.edit { it[stringPreferencesKey(PrefKeys.APP_LANG)] = langCode }
    }

    override suspend fun updateTheme(theme: AppThemeMode) {
        targetDataStore.edit { it[stringPreferencesKey(PrefKeys.APP_THEME)] = theme.name }
    }

    override suspend fun updateBinaryPref(format: BinaryPrefFormat) {
        targetDataStore.edit { it[stringPreferencesKey(PrefKeys.BINARY_PREF)] = format.name }
    }

    override suspend fun updateNativeLibVersion(version: NativeLibAuthors) {
        targetDataStore.edit { it[stringPreferencesKey(PrefKeys.LIB_VERSION)] = version.name }
    }

    override suspend fun updateActsCount(count: Int) {
        targetDataStore.edit { it[intPreferencesKey(PrefKeys.ACTS_COUNT)] = count }
    }

    override suspend fun toggleImmersiveMode(isEnabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.IMMERSIVE_MODE)] = isEnabled }
    }

    override suspend fun toggleExecString(isEnabled: Boolean) {
        targetDataStore.edit { it[booleanPreferencesKey(PrefKeys.EXEC_STRING)] = isEnabled }
    }

    override suspend fun updateCustomFontPath(filePath: String) {
        targetDataStore.edit { prefs ->
            prefs[stringPreferencesKey(PrefKeys.CUSTOM_FONT_PATH)] = filePath
        }
    }
}
