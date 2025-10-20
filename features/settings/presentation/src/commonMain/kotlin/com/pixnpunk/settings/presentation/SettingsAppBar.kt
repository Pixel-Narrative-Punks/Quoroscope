package com.pixnpunk.settings.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.router.stack.active

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAppBar(
    component: SettingsComponent
) {
    MediumTopAppBar(
        title = { Text("Settings") },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (component.childStack.active.instance is SettingsComponent.Child.RootChild) {
                        component.onDismissed()
                    } else {
                        component.backToRoot()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    )
}