package com.pixnpunk.stock.presentation.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pixnpunk.stock.presentation.RealStockComponent
import com.pixnpunk.stock.presentation.StockComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDefaultBottomBar(
    component: StockComponent,
    activeComponent: StockComponent.Child,
    isStockChildren: Boolean,
    scrollBehavior: BottomAppBarScrollBehavior
) {
    BottomAppBar(
        scrollBehavior = scrollBehavior
    ) {
        NavigationBarItem(
            selected = activeComponent is StockComponent.Child.AddonsChild,
            onClick = { component.navigateTo(RealStockComponent.ChildConfig.Addons) },
            icon = {
                if (activeComponent is StockComponent.Child.AddonsChild) {
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
            label = { Text("Addons") },
            alwaysShowLabel = false
        )
        NavigationBarItem(
            selected = isStockChildren,
            onClick = { component.navigateTo(RealStockComponent.ChildConfig.Stock) },
            icon = {
                if (isStockChildren) {
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
            },
            label = { Text("Stock") },
            alwaysShowLabel = false
        )
        NavigationBarItem(
            selected = activeComponent is StockComponent.Child.NewsChild,
            onClick = { component.navigateTo(RealStockComponent.ChildConfig.News) },
            icon = {
                if (activeComponent is StockComponent.Child.NewsChild) {
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
            },
            label = { Text("News") },
            alwaysShowLabel = false
        )
    }
}