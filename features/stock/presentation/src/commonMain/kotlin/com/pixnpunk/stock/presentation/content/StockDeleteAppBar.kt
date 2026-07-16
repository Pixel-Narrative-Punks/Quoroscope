package com.pixnpunk.stock.presentation.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.pluralStringResource
import quoroscope.features.stock.presentation.generated.resources.Res
import quoroscope.features.stock.presentation.generated.resources.selected_items_count

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDeleteAppBar(
    deleteSetSize: Int,
    stopDeleteMode: () -> Unit,
    selectAll: () -> Unit,
    deleteEntries: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                pluralStringResource(
                    Res.plurals.selected_items_count,
                    deleteSetSize,
                    deleteSetSize
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = stopDeleteMode) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = selectAll) {
                Icon(
                    imageVector = Icons.Default.SelectAll,
                    contentDescription = null
                )
            }
            IconButton(onClick = deleteEntries) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        },
    )
}