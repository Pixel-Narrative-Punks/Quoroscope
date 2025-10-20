package com.pixnpunk.addons.presentation.mvi

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.pixnpunk.addons.domain.usecase.GetAllAddonsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddonsExecutor(
    private val getAllAddonsUseCase: GetAllAddonsUseCase
) : CoroutineExecutor<AddonsStore.Intent, AddonsStore.Action, AddonsStore.State, AddonsStore.Message, AddonsStore.Label>() {
    private val outScope = CoroutineScope(Dispatchers.Default)

    override fun executeIntent(intent: AddonsStore.Intent) {
        super.executeIntent(intent)
    }

    override fun executeAction(action: AddonsStore.Action) {
        when (action) {
            is AddonsStore.Action.InitialLoadAddonsList -> {
                outScope.launch {
                    val addonsList = getAllAddonsUseCase.execute()
                    scope.launch { dispatch(AddonsStore.Message.UpdateAddonsList(addonsList)) }
                }
            }
        }
    }

    override fun dispose() {
        super.dispose()
        outScope.cancel()
    }
}