package com.pixnpunk.dialogs.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import coil3.compose.AsyncImage
import com.pixnpunk.dto.GameEntry

@Composable
fun DialogAddGameContent(
    entry: GameEntry,
    selectFile: () -> Unit,
    selectFolder: () -> Unit,
    addGame: (GameEntry) -> Unit,
    buttonText: String
) {
    var gameName by rememberSaveable { mutableStateOf(entry.title) }
    var gameAuthor by rememberSaveable { mutableStateOf(entry.author) }
    var gameVersion by rememberSaveable { mutableStateOf(entry.version) }

    val isButtonEnabled = gameName.isNotBlank() && entry.gameDirPath.isNotEmpty()

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
            onClick = selectFile
        ) {
            if (entry.gameIconPath.isNotEmpty()) {
                AsyncImage(
                    model = entry.gameIconPath,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Icon selected")
            } else {
                Text("Select icon for game")
            }
        }
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = selectFolder
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
                addGame(
                    entry.copy(
                        title = gameName,
                        author = gameAuthor,
                        version = gameVersion
                    )
                )
            },
            enabled = isButtonEnabled
        ) {
            Text(buttonText)
        }
    }
}