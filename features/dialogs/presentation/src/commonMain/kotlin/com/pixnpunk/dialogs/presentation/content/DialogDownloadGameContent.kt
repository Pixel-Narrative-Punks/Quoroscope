package com.pixnpunk.dialogs.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixnpunk.dialogs.presentation.DialogsComponent

@Composable
fun DialogDownloadGameContent(
    component: DialogsComponent
) {
    val entry = component.dialogsConfig.unfilledEntry

    var gameName by rememberSaveable { mutableStateOf(entry.title) }
    var gameAuthor by rememberSaveable { mutableStateOf(entry.author) }
    var gameVersion by rememberSaveable { mutableStateOf(entry.version) }

    val isButtonEnabled = gameName.isNotBlank()

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = gameName,
            onValueChange = { gameName = it },
            singleLine = true,
            label = { Text("Game name") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = gameAuthor,
            onValueChange = { gameAuthor = it },
            singleLine = true,
            label = { Text("Game author") }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = gameVersion,
            onValueChange = { gameVersion = it },
            singleLine = true,
            label = { Text("Game version") }
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = component.selectFolder
        ) {
            if (entry.gameDirPath.isNotEmpty()) {
                Text("Folder selected")
            } else {
                Text("Select folder where game stored")
            }
        }
        FilledTonalButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                component.downloadNewEntry(
                    entry.copy(
                        title = gameName,
                        author = gameAuthor,
                        version = gameVersion
                    )
                )
                component.onDismissed()
            },
            enabled = isButtonEnabled
        ) {
            Text("Download a new game entry")
        }
    }
}