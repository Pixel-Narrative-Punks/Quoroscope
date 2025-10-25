package com.pixnpunk.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.FontTypeface
import com.pixnpunk.dto.GameSettings
import com.pixnpunk.dto.NativeLibAuthors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.Preferences
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsModule
    get() = module {
        singleOf(::SettingsRepository)
    }

class SettingsRepository : ViewModel() {
    private val _settingsFlow: MutableStateFlow<GameSettings> = MutableStateFlow(GameSettings())
    val settingsFlow: StateFlow<GameSettings> = _settingsFlow

    init {
//        AddonsUtils.publishAppSettings(it)
    }

    fun emitColorValue(state: ColorPickerState, color: Color) {
        viewModelScope.launch {
            _settingsFlow.emit(
                when (state) {
                    ColorPickerState.COLOR_TEXT -> GameSettings(textColor = color.toArgb())
                    ColorPickerState.COLOR_BACK -> GameSettings(backColor = color.toArgb())
                    ColorPickerState.COLOR_LINK -> GameSettings(linkColor = color.toArgb())
                }
            )
        }
    }

    fun emitFlow(prefFlow: MutableStateFlow<Preferences>) {
        viewModelScope.launch {
            prefFlow.collect {
                val setMap = it.asMap()
                if (setMap.entries.isNotEmpty()) {
                    val defValue = settingsFlow.value
                    viewModelScope.launch {
                        _settingsFlow.emit(
                            GameSettings(
                                typeface = FontTypeface.valueOf(
                                    setMap["fontStylePref"] as? String ?: defValue.typeface.name
                                ),
                                fontSize = setMap["fontSizePref"] as? Int ?: defValue.fontSize,
//                isUseGameFont = true,
                                isUseGameTextColor = setMap["useGameTextColorPref"] as? Boolean
                                    ?: defValue.isUseGameTextColor,
//                textColor = 0,
                                isUseGameBackgroundColor = setMap["useGameBackColorPref"] as? Boolean
                                    ?: defValue.isUseGameBackgroundColor,
//                backColor = 0,
                                isUseGameLinkColor = setMap["useGameLinkColorPref"] as? Boolean
                                    ?: defValue.isUseGameLinkColor,
//                linkColor = 0,

                                isImgLoadDis = setMap["useGameImagesPref"] as? Boolean
                                    ?: defValue.isImgLoadDis,
                                isAutoWidthImage = setMap["useAutoImageWidthPref"] as? Boolean
                                    ?: defValue.isAutoWidthImage,
                                imageWidth = setMap["manualImageWidthPref"] as? Int
                                    ?: defValue.imageWidth,
                                isAutoHeightImage = setMap["useAutoImageHeightPref"] as? Boolean
                                    ?: defValue.isAutoHeightImage,
                                imageHeight = setMap["manualImageHeightPref"] as? Int
                                    ?: defValue.imageHeight,
                                isUseFullscreenImages = setMap["useImmersImagesPref"] as? Boolean
                                    ?: defValue.isUseFullscreenImages,
                                isUseImageDebug = setMap["useGameImageDebug"] as? Boolean
                                    ?: defValue.isUseImageDebug,

                                isSoundEnabled = setMap["useGameAudioPref"] as? Boolean
                                    ?: defValue.isSoundEnabled,
                                isVideoMute = setMap["useMuteGameVideoPref"] as? Boolean
                                    ?: defValue.isVideoMute,
                                isUseMusicDebug = setMap["useGameAudioDebug"] as? Boolean
                                    ?: defValue.isUseMusicDebug,

                                isUseExecString = setMap["useGameExecStringPref"] as? Boolean
                                    ?: defValue.isUseExecString,
                                isImmersModeEnabled = setMap["appImmersiveModePref"] as? Boolean
                                    ?: defValue.isImmersModeEnabled,
                                nativeLibFrom = NativeLibAuthors.valueOf(
                                    setMap["appLibVerPref"] as? String
                                        ?: defValue.nativeLibFrom.name
                                ),
                                visActsCount = setMap["countActsPref"] as? Int
                                    ?: defValue.visActsCount,
                                language = setMap["appLangPref"] as? String ?: defValue.language,
                                theme = setMap["appThemePref"] as? String ?: defValue.theme
                            )
                        )
                    }
                }
            }
        }
    }
}
