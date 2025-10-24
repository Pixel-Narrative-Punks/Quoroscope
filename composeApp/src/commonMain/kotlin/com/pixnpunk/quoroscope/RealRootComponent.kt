package com.pixnpunk.quoroscope

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.router.panels.Panels
import com.arkivanov.decompose.router.panels.PanelsNavigation
import com.arkivanov.decompose.router.panels.activateDetails
import com.arkivanov.decompose.router.panels.activateExtra
import com.arkivanov.decompose.router.panels.childPanels
import com.arkivanov.decompose.router.panels.dismissDetails
import com.arkivanov.decompose.router.panels.dismissExtra
import com.arkivanov.decompose.router.panels.setMode
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.child
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.pixnpunk.addons.AddonsUtils
import com.pixnpunk.details.presentation.DetailsComponent
import com.pixnpunk.details.presentation.RealDetailsComponent
import com.pixnpunk.dialogs.presentation.DialogsComponent
import com.pixnpunk.dialogs.presentation.DialogsConfig
import com.pixnpunk.dialogs.presentation.DialogsState
import com.pixnpunk.dialogs.presentation.RealDialogsComponent
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.quoroscope.mvi.RealRootStore
import com.pixnpunk.quoroscope.mvi.RootStore
import com.pixnpunk.settings.presentation.RealSettingsComponent
import com.pixnpunk.settings.presentation.SettingsComponent
import com.pixnpunk.stock.presentation.RealStockComponent
import com.pixnpunk.stock.presentation.StockComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RealRootComponent(
    private val componentContext: ComponentContext,
    private val launchDirPicker: () -> Unit,
    private val launchEngineIntent: (Pair<GameEntry, Int>) -> Unit,
) : ComponentContext by componentContext, RootComponent, KoinComponent {

    private val addonsRepository: AddonsUtils = get()

    private val dialogsNavigation = SlotNavigation<DialogsConfig>()

    @OptIn(ExperimentalDecomposeApi::class)
    private val panelNavigation = PanelsNavigation<Unit, DetailsConfig, Unit>()
    private val store = instanceKeeper.getStore {
        RealRootStore(DefaultStoreFactory()).create()
    }

    fun showAddNewGameDialog(dirName: String, rootDirPath: String) {
        val oldConfig = dialogSlot.child?.configuration as? DialogsConfig
        if (oldConfig != null && oldConfig.dialogsState == DialogsState.DIALOG_DOWNLOAD_GAME) {
            dialogsNavigation.activate(
                oldConfig.copy(
                    unfilledEntry = oldConfig.unfilledEntry.copy(
                        gameDirPath = rootDirPath
                    )
                )
            )
        } else {
            dialogsNavigation.activate(
                DialogsConfig(
                    dialogsState = DialogsState.DIALOG_ADD_GAME,
                    unfilledEntry = GameEntry(
                        title = dirName,
                        gameDirPath = rootDirPath,
                    )
                )
            )
        }
    }

    private fun selGameDialog(entry: GameEntry) {
        if (!addonsRepository.receiveEngineAddons()) {
            dialogsNavigation.activate(
                DialogsConfig(
                    dialogsState = DialogsState.DIALOG_ERROR_ADDONS
                )
            )
            return
        }

        when (entry.gameFilesPath.size) {
            0 -> {
                dialogsNavigation.activate(
                    DialogsConfig(
                        dialogsState = DialogsState.DIALOG_ERROR_GAME,
                        unfilledEntry = entry
                    )
                )
            }

            1 -> {
                launchEngineIntent(entry to 0)
            }

            else -> {
                dialogsNavigation.activate(
                    DialogsConfig(
                        dialogsState = DialogsState.DIALOG_SELECT_RUN_GAME,
                        unfilledEntry = entry
                    )
                )
            }
        }
    }

    override val dialogSlot: Value<ChildSlot<*, DialogsComponent>> =
        childSlot(
            source = dialogsNavigation,
            key = "DialogsChildSlot",
            serializer = serializer(),
        ) { config, ctx ->
            RealDialogsComponent(
                componentContext = ctx,
                dialogsConfig = config,
                addNewEntry = { store.accept(RootStore.Intent.AddNewGame(it)) },
                downloadNewEntry = { store.accept(RootStore.Intent.DownloadNewGame(it)) },
                selectFolder = launchDirPicker,
                selectColor = {
                    store.accept(RootStore.Intent.UpdateSettingsColor(it.first, it.second))
                },
                doPlayGame = { entry, value -> launchEngineIntent(entry to value) },
                onDismissed = dialogsNavigation::dismiss
            )
        }

    @OptIn(ExperimentalDecomposeApi::class, ExperimentalSerializationApi::class)
    override val childPanel: Value<ChildPanels<*, StockComponent, *, DetailsComponent, *, SettingsComponent>> =
        childPanels(
            source = panelNavigation,
            serializers = Triple(
                Unit.serializer(),
                DetailsConfig.serializer(),
                Unit.serializer()
            ),
            initialPanels = { Panels(main = Unit) },
            handleBackButton = true,
            extraFactory = { _, ctx ->
                RealSettingsComponent(
                    componentContext = ctx,
                    showColorDialog = {
                        dialogsNavigation.activate(
                            DialogsConfig(
                                dialogsState = DialogsState.DIALOG_SELECT_COLOR,
                                colorPickerState = it
                            )
                        )
                    },
                    onDismissed = panelNavigation::dismissExtra
                )
            },
            mainFactory = { _, ctx ->
                RealStockComponent(
                    componentContext = ctx,
                    launchDirPicker = launchDirPicker,
                    navigateToDetails = { entryShow ->
                        panelNavigation.activateDetails(details = DetailsConfig(entryShow))
                    },
                    navigateToSettings = {
                        panelNavigation.activateExtra(Unit)
                    }
                )
            },
            detailsFactory = { cfg, ctx ->
                RealDetailsComponent(
                    componentContext = ctx,
                    entryShow = cfg.entryShow,
                    showSelectGameDialog = { selGameDialog(it) },
                    showEditGameDialog = { },
                    showDownloadGameDialog = {
                        dialogsNavigation.activate(
                            DialogsConfig(
                                dialogsState = DialogsState.DIALOG_DOWNLOAD_GAME,
                                unfilledEntry = it
                            )
                        )
                    },
                    onDismissed = panelNavigation::dismissDetails
                )
            }
        )

    @OptIn(ExperimentalDecomposeApi::class)
    override fun setMode(mode: ChildPanelsMode) {
        panelNavigation.setMode(mode)
    }

    @Serializable
    private data class DetailsConfig(val entryShow: GameEntry)
}