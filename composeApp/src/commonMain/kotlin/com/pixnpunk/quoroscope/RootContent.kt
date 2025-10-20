package com.pixnpunk.quoroscope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(
    component: RootComponent
) {
    val dialogSlot by component.dialogSlot.subscribeAsState()

    QuoroscopeTheme {
        dialogSlot.child?.instance?.also {
            DialogsMainContent(it)
        }

        ChildPanelsModeChangedEffect(component::setMode)

        ChildPanels(
            panels = component.childPanel,
            mainChild = { StockContent(it.instance) },
            detailsChild = { DetailsContent(it.instance) },
            extraChild = { SettingsContent(it.instance) },
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