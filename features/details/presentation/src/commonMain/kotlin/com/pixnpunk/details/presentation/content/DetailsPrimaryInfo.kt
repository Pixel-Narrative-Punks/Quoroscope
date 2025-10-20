package com.pixnpunk.details.presentation.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import com.pixnpunk.dto.GameEntry

@Composable
fun DetailsPrimaryInfo(
    entry: GameEntry
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(shape = MaterialTheme.shapes.small) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                leadingContent = {
                    AsyncImage(
                        modifier = Modifier.size(50.dp),
                        model = entry.gameIconPath,
                        error = rememberVectorPainter(Icons.Outlined.BrokenImage),
                        contentDescription = null,
                    )
                },
                headlineContent = { Text(entry.title) },
                supportingContent = {
                    Column {
                        AnimatedVisibility(
                            visible = entry.author.isNotEmpty()
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "By: ${entry.author}"
                            )
                        }
                        AnimatedVisibility(
                            visible = entry.portedBy.isNotEmpty()
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = "Ported by: ${entry.portedBy}"
                            )
                        }
                    }
                }
            )
        }
    }
}