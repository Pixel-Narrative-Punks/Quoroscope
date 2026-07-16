package com.pixnpunk.dialogs.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.dialogs.presentation.generated.resources.Res
import quoroscope.features.dialogs.presentation.generated.resources.cancel_button_description
import quoroscope.features.dialogs.presentation.generated.resources.input_button_description

@Composable
fun DialogInputValueContent(
    isError: Boolean,
    initialValue: String,
    supportValue: String,
    supportErrorValue: String,
    keyboardOptions: KeyboardOptions,
    label: @Composable () -> Unit,
    inputValue: (String) -> Unit,
    cleanError: () -> Unit,
    onDismiss: () -> Unit
) {
    var enterValue by remember {
        mutableStateOf(
            htmlToAnnotatedString(initialValue).text
        )
    }

    val isEnable = when (keyboardOptions.keyboardType) {
        KeyboardType.Number -> enterValue.toIntOrNull() != null
        KeyboardType.Unspecified -> true
        else -> false
    }

    Column(
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = enterValue,
            onValueChange = { enterValue = it; if (isError) cleanError() },
            label = label,
            singleLine = true,
            isError = isError,
            keyboardOptions = keyboardOptions,
            supportingText = {
                if (isError) {
                    val annotatedError = remember(supportValue) {
                        htmlToAnnotatedString(supportErrorValue)
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clearAndSetSemantics { contentDescription = annotatedError.text },
                        text = annotatedError,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (supportValue.isNotBlank()) {
                    val annotatedSupport = remember(supportValue) {
                        htmlToAnnotatedString(supportValue)
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clearAndSetSemantics { contentDescription = annotatedSupport.text },
                        text = annotatedSupport
                    )
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(Res.string.cancel_button_description))
            }
            TextButton(
                onClick = { inputValue(enterValue); onDismiss() },
                enabled = isEnable
            ) {
                Text(stringResource(Res.string.input_button_description))
            }
        }
    }
}