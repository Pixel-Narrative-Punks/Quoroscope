package com.pixnpunk.dialogs.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixnpunk.dialogs.presentation.DialogsComponent
import com.pixnpunk.dialogs.presentation.DialogsState

@Composable
fun DialogErrorContent(
    state: DialogsState,
    gameTitle: String,
    onDismiss: () -> Unit
) {
    val message = when (state) {
        DialogsState.DIALOG_ERROR_GAME -> {
            "The game files for the $gameTitle are missing!"
        }

        DialogsState.DIALOG_ERROR_ADDONS -> {
            "QSPlugin not found. You need to install the addon manually"
        }

        else -> ""
    }

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 16.dp),
            text = message,
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    }
}