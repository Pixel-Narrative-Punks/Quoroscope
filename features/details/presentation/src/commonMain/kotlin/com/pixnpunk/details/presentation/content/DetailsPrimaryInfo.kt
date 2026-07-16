package com.pixnpunk.details.presentation.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pixnpunk.dto.GameEntry
import org.jetbrains.compose.resources.stringResource
import quoroscope.features.details.presentation.generated.resources.Res
import quoroscope.features.details.presentation.generated.resources.author_title
import quoroscope.features.details.presentation.generated.resources.icon_description
import quoroscope.features.details.presentation.generated.resources.ported_by_title

@Composable
fun DetailsPrimaryInfo(
    entry: GameEntry
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(shape = MaterialTheme.shapes.small) {
            ListItem(
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                leadingContent = {
                    AsyncImage(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(MaterialTheme.shapes.medium),
                        model = entry.gameIconPath,
                        error = rememberVectorPainter(Icons.Outlined.BrokenImage),
                        contentDescription = stringResource(Res.string.icon_description, entry.title),
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
                                text = stringResource(Res.string.author_title, entry.author)
                            )
                        }
                        AnimatedVisibility(
                            visible = entry.portedBy.isNotEmpty()
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                text = stringResource(Res.string.ported_by_title, entry.portedBy)
                            )
                        }
                    }
                }
            )
        }
    }
}