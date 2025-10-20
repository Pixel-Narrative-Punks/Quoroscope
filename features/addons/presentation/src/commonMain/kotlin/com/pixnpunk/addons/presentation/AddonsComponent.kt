package com.pixnpunk.addons.presentation

import com.pixnpunk.addons.presentation.mvi.AddonsStore
import kotlinx.coroutines.flow.StateFlow

sealed interface AddonsComponent {
    val model: StateFlow<AddonsStore.State>
}