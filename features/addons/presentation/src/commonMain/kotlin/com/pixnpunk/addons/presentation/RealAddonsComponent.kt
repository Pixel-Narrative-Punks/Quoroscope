package com.pixnpunk.addons.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.pixnpunk.addons.presentation.mvi.AddonsStore
import com.pixnpunk.addons.presentation.mvi.RealAddonsStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class RealAddonsComponent(
    private val componentContext: ComponentContext
) : ComponentContext by componentContext, AddonsComponent {

    private val store = instanceKeeper.getStore {
        RealAddonsStore(DefaultStoreFactory()).create()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddonsStore.State> = store.stateFlow
}