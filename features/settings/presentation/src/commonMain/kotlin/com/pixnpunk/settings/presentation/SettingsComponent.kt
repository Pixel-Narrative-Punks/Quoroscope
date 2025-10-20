package com.pixnpunk.settings.presentation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

sealed interface SettingsComponent {
    val childStack: Value<ChildStack<*, Child>>
    val onDismissed: () -> Unit

    fun moveToChild(config: RealSettingsComponent.ChildConfig)
    fun backToRoot()

    sealed class Child {
        class AudioChild() : Child()
        class GeneralChild() : Child()
        class PicturesChild() : Child()
        class RootChild() : Child()
        class TextChild() : Child()
    }
}