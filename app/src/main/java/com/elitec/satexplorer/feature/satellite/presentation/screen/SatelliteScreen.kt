package com.elitec.satexplorer.feature.satellite.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elitec.satexplorer.feature.satellite.presentation.viewmodel.SatelliteSearchViewModel
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SatelliteScreen(
    modifier: Modifier = Modifier,
    viewModel: SatelliteSearchViewModel = koinViewModel(),
    onSatelliteSelected: (Satellite) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = "MISSION CONTROL"
        )

        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                    Text("Search satellite")
                }
            },
            placeholder = {
                Text("Search by name or NORAD")
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = viewModel::submitSearch,
                modifier = Modifier.weight(1f)
            ) {
                Text("Search")
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = "Catalog results"
                    )
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = state.totalItems.toString()
                    )
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(SatelliteType.toList()) { satelliteType ->
                val isSelected = state.selectedType == satelliteType
                Button(
                    onClick = { viewModel.onTypeSelected(satelliteType) },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animateColorAsState(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            label = "satellite_type_container"
                        ).value,
                        contentColor = animateColorAsState(
                            if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            label = "satellite_type_content"
                        ).value
                    )
                ) {
                    Text(satelliteType.name)
                }
            }
        }

        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.satellites, key = { it.noradId }) { satellite ->
                    SatelliteCatalogCard(
                        satellite = satellite,
                        onClick = { onSatelliteSelected(satellite) }
                    )
                }

                item {
                    when {
                        state.errorMessage != null -> {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = state.errorMessage.toString(),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        state.isAppending -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.hasNextPage -> {
                            Button(
                                onClick = viewModel::loadNextPage,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Load more")
                            }
                        }

                        state.satellites.isEmpty() -> {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "No satellites found for the current search.",
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SatelliteCatalogCard(
    satellite: Satellite,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge,
                        text = satellite.name
                    )
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = "NORAD ${satellite.noradId}"
                    )
                }
                Surface(
                    modifier = Modifier.width(140.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = signalGreen.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = satellite.type.name,
                        color = signalGreen,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SatelliteMetric(
                    label = "Inclination",
                    value = "${"%.2f".format(Locale.US, satellite.tle.inclination)} deg",
                    modifier = Modifier.weight(1f)
                )
                SatelliteMetric(
                    label = "Mean motion",
                    value = "${"%.4f".format(Locale.US, satellite.tle.meanMotion)} rev/day",
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = "TLE epoch: ${formatEpoch(satellite.tle.epoch)}"
            )
        }
    }
}

@Composable
private fun SatelliteMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = label
            )
            Text(
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                text = value
            )
        }
    }
}

private fun formatEpoch(epochMillis: Long): String {
    if (epochMillis <= 0L) {
        return "Unknown"
    }

    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
    return formatter.format(Date(epochMillis))
}
