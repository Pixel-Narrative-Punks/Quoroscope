package com.pixnpunk.settings.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.pixnpunk.settings.presentation.contents.SettingsAudioContent
import com.pixnpunk.settings.presentation.contents.SettingsGeneralContent
import com.pixnpunk.settings.presentation.contents.SettingsImageContent
import com.pixnpunk.settings.presentation.contents.SettingsRootContent
import com.pixnpunk.settings.presentation.contents.SettingsTextContent
import com.pixnpunk.utils.SettingsUtil
import me.zhanghai.compose.preference.LocalPreferenceFlow
import me.zhanghai.compose.preference.ProvidePreferenceLocals

@Composable
fun SettingsContent(
    component: SettingsComponent
) {
    val stack by component.childStack.subscribeAsState()

    ProvidePreferenceLocals {
        val flow = LocalPreferenceFlow.current

        LaunchedEffect(flow.value) {
            flow.collect { SettingsUtil.emitValue(it) }
        }

        Scaffold(
            topBar = { SettingsAppBar(component) }
        ) { paddingValues ->
            Children(
                stack = stack
            ) { child ->
                when (child.instance) {
                    is SettingsComponent.Child.AudioChild -> SettingsAudioContent(
                        component,
                        paddingValues
                    )

                    is SettingsComponent.Child.GeneralChild -> SettingsGeneralContent(
                        component,
                        paddingValues
                    )

                    is SettingsComponent.Child.PicturesChild -> SettingsImageContent(
                        component,
                        paddingValues
                    )

                    is SettingsComponent.Child.TextChild -> SettingsTextContent(
                        component,
                        paddingValues
                    )

                    is SettingsComponent.Child.RootChild -> SettingsRootContent(
                        component,
                        paddingValues
                    )
                }
            }
        }
    }
}