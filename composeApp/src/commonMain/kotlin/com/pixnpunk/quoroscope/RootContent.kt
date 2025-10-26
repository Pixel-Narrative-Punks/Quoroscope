package com.pixnpunk.quoroscope

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanels
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanelsAnimators
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.pixnpunk.details.presentation.DetailsContent
import com.pixnpunk.dialogs.presentation.DialogsMainContent
import com.pixnpunk.quoroscope.theme.QuoroscopeTheme
import com.pixnpunk.settings.presentation.SettingsContent
import com.pixnpunk.stock.presentation.StockContent
import com.pixnpunk.utils.SettingsRepository
import me.zhanghai.compose.preference.LocalPreferenceFlow
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(
    component: RootComponent,
    settingsRepository: SettingsRepository
) {
    val dialogSlot by component.dialogSlot.subscribeAsState()

    ProvidePreferenceLocals {
        val pref by LocalPreferenceFlow.current.collectAsState()
        settingsRepository.emitMap(pref.asMap())
    }

    val settings by settingsRepository.settingsFlow.collectAsState()

    QuoroscopeTheme(
        darkTheme =
            when {
                settings.theme.contains("auto") -> isSystemInDarkTheme()
                settings.theme.contains("dark") -> true
                settings.theme.contains("light") -> false
                else -> false
            }
    ) {
        dialogSlot.child?.instance?.also {
            DialogsMainContent(it, settingsRepository)
        }

        ChildPanelsModeChangedEffect(component::setMode)

        ChildPanels(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            panels = component.childPanel,
            mainChild = { StockContent(it.instance, settings) },
            detailsChild = { DetailsContent(it.instance, settings) },
            extraChild = { SettingsContent(it.instance, settingsRepository) },
            animators = ChildPanelsAnimators(single = fade() + scale(), dual = fade() to fade()),
//            predictiveBackParams = { // See the docs below
//                PredictiveBackParams(
//                    backHandler = component.backHandler,
//                    onBack = component::onBackClicked,
//                    animatable = ::materialPredictiveBackAnimatable,
//                )
//            },
        )
    }
}