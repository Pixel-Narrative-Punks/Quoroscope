package com.pixnpunk.quoroscope

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.pixnpunk.addons.presentation.AddonsComponent
import com.pixnpunk.details.presentation.RealDetailsComponent
import com.pixnpunk.dialogs.presentation.DialogsComponent
import com.pixnpunk.dialogs.presentation.DialogsState
import com.pixnpunk.dto.ColorPickerState
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.dto.InputState
import com.pixnpunk.dto.OutputState
import com.pixnpunk.dto.SettingsEntity
import com.pixnpunk.news.presentation.NewsComponent
import com.pixnpunk.settings.presentation.RealSettingsComponent
import com.pixnpunk.stock.presentation.StockComponent
import kotlinx.coroutines.flow.StateFlow

sealed interface RootComponent {
    val dialogSlot: Value<ChildSlot<*, DialogsComponent>>
    val settingsState: StateFlow<SettingsEntity>

    val rootStack: Value<ChildStack<*, Child>>
    val detailsSlot: Value<ChildSlot<RealRootComponent.DetailsConfig, RealDetailsComponent>>
    val settingsSlot: Value<ChildSlot<RealRootComponent.SettingsConfig, RealSettingsComponent>>

    fun doShowDialog()
    fun doShowDialog(state: DialogsState, entry: GameEntry)
    fun doShowDialog(pickerState: ColorPickerState)
    fun doShowDialog(inputState: InputState)
    fun doShowDialog(outputValue: String, outputState: OutputState)

    fun navigateToAddons()
    fun navigateToStock()
    fun navigateToNews()

    sealed interface Child {
        data class Addons(val component: AddonsComponent) : Child
        data class Stock(val component: StockComponent) : Child
        data class News(val component: NewsComponent) : Child
    }
}