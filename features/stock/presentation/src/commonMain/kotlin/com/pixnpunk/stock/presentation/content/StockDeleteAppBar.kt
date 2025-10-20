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
import androidx.compose.runtime.snapshots.SnapshotStateSet
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.stock.presentation.StockComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDeleteAppBar(
    component: StockComponent,
    entries: SnapshotStateSet<GameEntry>,
    allEntries: Set<GameEntry>
) {
    TopAppBar(
        title = { Text("Selected ${entries.size} element's") },
        navigationIcon = {
            IconButton(onClick = {
                entries.clear()
                component.stopDeleteMode()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(onClick = { entries.addAll(allEntries) }) {
                Icon(
                    imageVector = Icons.Default.SelectAll,
                    contentDescription = ""
                )
            }
            IconButton(onClick = { component.deleteEntries(entries) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = ""
                )
            }
        },
    )
}