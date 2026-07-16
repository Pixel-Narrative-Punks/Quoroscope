package com.pixnpunk.dialogs.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.utils.FileUtil.receiveBaseName
import com.pixnpunk.utils.FileUtil.receiveDirRelPath
import com.pixnpunk.utils.FileUtil.receiveFileRelPath
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.dialogs.presentation.generated.resources.Res
import quoroscope.features.dialogs.presentation.generated.resources.author_field
import quoroscope.features.dialogs.presentation.generated.resources.cancel_button_description
import quoroscope.features.dialogs.presentation.generated.resources.file_selector_title
import quoroscope.features.dialogs.presentation.generated.resources.folder_selector_title
import quoroscope.features.dialogs.presentation.generated.resources.title_field
import quoroscope.features.dialogs.presentation.generated.resources.version_field

@Composable
fun DialogAddGameContent(
    entry: GameEntry,
    selectFile: () -> Unit,
    deselectFile: (String) -> Unit,
    selectDir: () -> Unit,
    deselectDir: () -> Unit,
    selectFiles: () -> Unit,
    addGame: (GameEntry) -> Unit,
    onDismiss: () -> Unit,
    isAddLocalGame: Boolean,
    buttonText: String
) {
    var gameName by remember { mutableStateOf(entry.title) }
    var gameAuthor by remember { mutableStateOf(entry.author) }
    var gameVersion by remember { mutableStateOf(entry.version) }

    LaunchedEffect(entry.gameDirPath) {
        if (gameName.isEmpty() && entry.gameDirPath.isNotBlank()) {
            gameName = entry.gameDirPath.receiveBaseName()
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameCoverSelector(
            imagePath = entry.gameIconPath,
            onClick = selectFile,
            onClear = { deselectFile("") }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = gameName,
            onValueChange = { gameName = it },
            singleLine = true,
            label = { Text(stringResource(Res.string.title_field)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = gameAuthor,
                onValueChange = { gameAuthor = it },
                singleLine = true,
                label = { Text(stringResource(Res.string.author_field)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                modifier = Modifier.weight(0.5f),
                value = gameVersion,
                onValueChange = { gameVersion = it },
                singleLine = true,
                label = { Text(stringResource(Res.string.version_field)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }

        if (isAddLocalGame) {
            HorizontalDivider()

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isDirSelected = entry.gameDirPath.isNotEmpty()

                FilterChip(
                    selected = isDirSelected,
                    onClick = if (isDirSelected) deselectDir else selectDir,
                    label = {
                        Text(
                            if (isDirSelected) entry.gameDirPath.receiveDirRelPath()
                            else stringResource(Res.string.folder_selector_title)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isDirSelected) Icons.Default.Folder else Icons.Default.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = if (isDirSelected) {
                        { Icon(Icons.Default.Close, contentDescription = null) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                )

                if (isDirSelected) {
                    entry.gameFilesPath.forEach { filePath ->
                        InputChip(
                            selected = true,
                            onClick = { deselectFile(filePath) },
                            label = { Text(filePath.receiveFileRelPath()) },
                            avatar = {
                                Icon(
                                    Icons.AutoMirrored.Filled.InsertDriveFile,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = { Icon(Icons.Default.Close, contentDescription = null) },
                        )
                    }

                    AssistChip(
                        onClick = selectFiles,
                        label = { Text(stringResource(Res.string.file_selector_title)) },
                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel_button_description))
            }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    addGame(
                        entry.copy(
                            title = gameName,
                            author = gameAuthor,
                            version = gameVersion
                        )
                    )
                },
                enabled =
                    if (isAddLocalGame) {
                        gameName.isNotBlank() && entry.gameDirPath.isNotEmpty()
                    } else {
                        gameName.isNotBlank()
                    }
            ) {
                Text(buttonText)
            }
        }
    }
}