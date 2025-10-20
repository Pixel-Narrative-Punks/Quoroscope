package com.pixnpunk.addons.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AddonsContent(
    component: AddonsComponent
) {
    val state by component.model.collectAsState()
    LazyColumn {
        items(state.addonsList) {
            ListItem(
                headlineContent = { Text(it.title) },
                supportingContent = { Text(it.author) }
            )
        }
    }
}