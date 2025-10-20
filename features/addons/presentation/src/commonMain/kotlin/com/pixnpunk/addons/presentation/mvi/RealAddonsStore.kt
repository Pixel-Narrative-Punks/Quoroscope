package com.pixnpunk.addons.presentation.mvi

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RealAddonsStore(
    private val storeFactory: StoreFactory
) : KoinComponent {
    internal fun create(): AddonsStore = AddonsImpl()

    private inner class AddonsImpl : AddonsStore,
        Store<AddonsStore.Intent, AddonsStore.State, AddonsStore.Label> by storeFactory.create(
            name = "AddonsStore",
            initialState = AddonsStore.State(),
            bootstrapper = SimpleBootstrapper(
                AddonsStore.Action.InitialLoadAddonsList
            ),
            executorFactory = { AddonsExecutor(get()) },
            reducer = { message ->
                when (message) {
                    is AddonsStore.Message.UpdateAddonsList -> copy(message.addonsList)
                }
            }
        )
}