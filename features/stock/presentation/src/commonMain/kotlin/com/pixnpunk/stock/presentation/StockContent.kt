package com.pixnpunk.stock.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.pixnpunk.addons.presentation.AddonsContent
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.news.presentation.NewsContent
import com.pixnpunk.stock.presentation.content.StockDefaultBottomBar
import com.pixnpunk.stock.presentation.content.StockDeleteAppBar
import com.pixnpunk.stock.presentation.content.StockFloatButton
import com.pixnpunk.stock.presentation.content.StockInstalledChild
import com.pixnpunk.stock.presentation.content.StockLibraryChild
import com.pixnpunk.stock.presentation.content.StockSearchAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockContent(
    component: StockComponent
) {
    val stack by component.childStack.subscribeAsState()
    val activeComponent = stack.active.instance
    val model by component.model.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    val gridState = rememberLazyGridState()
    val listState = rememberLazyListState()

    val setToDelete = rememberSaveable { mutableStateSetOf<GameEntry>() }
    val setAllEntries = rememberSaveable { mutableStateSetOf<GameEntry>() }

    val isAtTop by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
        }
    }
    val isStockChildren = when (activeComponent) {
        is StockComponent.Child.InstalledChild, is StockComponent.Child.LibraryChild -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isStockChildren && model.isDeleteModeActive) {
                StockDeleteAppBar(
                    component = component,
                    entries = setToDelete,
                    allEntries = setAllEntries
                )
            } else {
                StockSearchAppBar(component, isStockChildren)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isAtTop
                        && activeComponent is StockComponent.Child.InstalledChild
                        && !model.isDeleteModeActive,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StockFloatButton(
                    onClick = component.launchDirPicker,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add new game"
                        )
                    }
                )
            }
            AnimatedVisibility(
                visible = !isAtTop
                        && activeComponent is StockComponent.Child.LibraryChild,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StockFloatButton(
                    onClick = {
                        scope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowUpward,
                            contentDescription = "Scroll to top"
                        )
                    }
                )
            }
        },
        bottomBar = {
            if (!model.isDeleteModeActive) {
                StockDefaultBottomBar(
                    component,
                    activeComponent,
                    isStockChildren,
                    scrollBehavior
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Children(
            modifier = Modifier.padding(paddingValues),
            stack = stack
        ) { child ->
            when (val instance = child.instance) {
                is StockComponent.Child.AddonsChild -> AddonsContent(instance.component)
                is StockComponent.Child.NewsChild -> NewsContent(instance.component)
                is StockComponent.Child.InstalledChild -> {
                    StockInstalledChild(
                        component,
                        listState,
                        setToDelete,
                        setAllEntries,
                        model.isDeleteModeActive
                    )
                }

                is StockComponent.Child.LibraryChild -> StockLibraryChild(component, gridState)
            }
        }
    }
}