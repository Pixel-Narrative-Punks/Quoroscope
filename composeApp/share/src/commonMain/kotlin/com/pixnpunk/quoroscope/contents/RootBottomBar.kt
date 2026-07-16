package com.pixnpunk.quoroscope.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import com.pixnpunk.stock.presentation.StockComponent

@Composable
fun RootBottomBar(
    isAddonsActive: Boolean,
    isStockActive: Boolean,
    isNewsActive: Boolean,
    activateAddons: () -> Unit,
    activateStock: () -> Unit,
    activateNews: () -> Unit,
    content: @Composable (() -> Unit)
) {
    NavigationSuiteScaffold(
        content = content,
        navigationItemVerticalArrangement = Arrangement.Center,
        navigationItems = {
            NavigationSuiteItem(
                selected = isAddonsActive,
                onClick = activateAddons,
                label = { Text("Addons") },
                icon = {
                    if (isAddonsActive) {
                        Icon(
                            imageVector = Icons.Filled.Extension,
                            contentDescription = ""
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Extension,
                            contentDescription = ""
                        )
                    }
                },
            )
            NavigationSuiteItem(
                selected = isStockActive,
                onClick = activateStock,
                label = { Text("Stock") },
                icon = {
                    if (isStockActive) {
                        Icon(
                            imageVector = Icons.Filled.Archive,
                            contentDescription = ""
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Archive,
                            contentDescription = ""
                        )
                    }
                }
            )
            NavigationSuiteItem(
                selected = isNewsActive,
                onClick = activateNews,
                label = { Text("News") },
                icon = {
                    if (isNewsActive) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Feed,
                            contentDescription = ""
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Feed,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    )
}