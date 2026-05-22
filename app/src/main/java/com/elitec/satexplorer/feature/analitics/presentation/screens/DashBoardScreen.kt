package com.elitec.satexplorer.feature.analitics.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.feature.analitics.presentation.model.DashboardUiState
import com.elitec.satexplorer.feature.analitics.presentation.model.LiveLogItem
import com.elitec.satexplorer.feature.analitics.presentation.model.LogType
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import com.elitec.satexplorer.infrastructure.presentation.theme.signalAmber
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import com.elitec.satexplorer.infrastructure.presentation.theme.telemetryRed
import kotlin.random.Random

@Composable
fun DashBoardScreen(
    uiState: DashboardUiState,
    onNotifyOnPass: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(0.3f),
                                        MaterialTheme.colorScheme.surfaceContainer
                                    )
                                )
                            )
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SatelliteAlt,
                            contentDescription = "Satellite icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Sats trackers"
                        )
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            text = uiState.trackedSatellites.toString()
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(topEnd = 20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(0.3f),
                                        MaterialTheme.colorScheme.surfaceContainer,
                                    ),
                                )
                            )
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = "Satellite icon",
                            tint = signalAmber
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Visible tonight"
                        )
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            text = uiState.visibleTonight.toString()
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(bottomStart = 20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceContainer,
                                        MaterialTheme.colorScheme.primary.copy(0.3f)

                                    )
                                )
                            )
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Avg. velocity",
                            tint = Color.Blue
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Avg. velocity"
                        )
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            text = "${uiState.averageVelocityKmh} Km/h"
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surfaceContainer,
                                        MaterialTheme.colorScheme.primary.copy(0.3f)

                                    )
                                )
                            )
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Avg. velocity",
                            tint = telemetryRed
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Congestion alert"
                        )
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            text = uiState.congestionAlert
                        )
                    }
                }
            }
        }
        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = "PROXIMITY ALERT"
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.height(330.dp).fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.tianlong),
                contentDescription = "satellite image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surfaceVariant.copy(0.8f),
                                MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = "T-MINUS ${uiState.proximityAlert?.tMinus ?: "--:--"}"
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                            text = "Tracking target"
                        )
                        Text(
                            style = MaterialTheme.typography.headlineSmall,
                            text = uiState.proximityAlert?.satelliteName ?: "NO SATELLITE SELECTED"
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            Modifier.weight(1f).fillMaxWidth(), Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                text = "AZIMUTH"
                            )
                            Text(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                text = uiState.proximityAlert?.azimuth ?: "--"
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                text = "ELEVATION"
                            )
                            Text(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                text = uiState.proximityAlert?.elevation ?: "--"
                            )
                        }
                    }
                    Button(
                        border = BorderStroke(
                            1.dp,
                            Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                    Color.Transparent
                                )
                            )
                        ),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background.copy(0.6f),
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = onNotifyOnPass
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "notification icon"
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "NOTIFY ON PASS"
                            )
                        }
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = "LIVE ACTIVITY FEED"
            )
            Text(
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                text = "Export logs"
            )
        }
        Surface(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            LiveLogBox(
                logList = uiState.logs,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Surface(
            shadowElevation = 5.dp,
            tonalElevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = "System Health"
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = "check icon",
                        tint = signalGreen
                    )
                }
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = "API RESPONSIVENESS"
                        )
                        Text(
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                            text = "${uiState.apiResponsivenessMs} ms",
                            color = signalGreen
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.Transparent
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface.copy(0.5f),
                                shape = RoundedCornerShape(10.dp),
                            )

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxWidth(uiState.apiHealthPercent.coerceIn(0.05f, 1f))
                                .background(
                                    shape = RoundedCornerShape(10.dp),
                                    color = signalGreen
                                )
                        ) { }
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveLogBox(
    logList: List<LiveLogItem>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            logList.forEach { log ->
                LiveLogBoxItem(log = log)
            }
        }
    }
}

@Composable
private fun LiveLogBoxItem(
    log: LiveLogItem,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(top = 5.dp),
            verticalArrangement = Arrangement. spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = log.time,
                style = MaterialTheme.typography.bodySmall
            )
            VerticalDivider(
                thickness = 1.dp,
                modifier = Modifier.size(30.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = "STARLINK-3821"
                )
                val logColor = when(log.type) {
                    LogType.TIME_OUT -> telemetryRed
                    LogType.LIVE -> signalGreen
                    LogType.LOW_ALTITUDE -> signalAmber
                    LogType.ORBIT_LOCKED -> signalGreen
                    LogType.SIGNAL_LOST -> telemetryRed
                }
                Surface(
                    color = logColor.copy(0.1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
                        fontWeight = FontWeight.Bold,
                        color = logColor,
                        text = log.type.name.replace("_", " ")
                    )
                }
            }
            Text(
                text = log.body
            )
        }
    }
}
