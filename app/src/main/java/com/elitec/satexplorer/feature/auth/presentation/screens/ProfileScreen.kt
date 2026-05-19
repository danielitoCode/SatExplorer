package com.elitec.satexplorer.feature.auth.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.elitec.satexplorer.R
import com.elitec.satexplorer.feature.auth.domain.entity.AccountState
import com.elitec.satexplorer.feature.auth.domain.entity.SystemSettingsConfiguration
import com.elitec.satexplorer.feature.auth.domain.entity.User
import com.elitec.satexplorer.feature.auth.domain.entity.UserRank
import com.elitec.satexplorer.infrastructure.domain.DistanceUnitsMetrics
import com.elitec.satexplorer.infrastructure.domain.VelocityUnitsMetrics
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import com.elitec.satexplorer.infrastructure.presentation.theme.signalAmber
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import com.elitec.satexplorer.infrastructure.presentation.theme.telemetryRed
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun ProfileScreen(
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileBox(
            user = user,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = "Systems Configurations"
        )
        RefreshRatingBox(
            onSaveButtonPress = {},
            profileConfigs = user.settingsConfiguration,
            modifier = Modifier.fillMaxWidth()
        )
        MetricsUnitsBox(
            onSaveButtonPress = {},
            profileSettings = user.settingsConfiguration,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            border = BorderStroke(2.dp,telemetryRed),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = telemetryRed.copy(0.1f),
                contentColor = telemetryRed
            ),
            onClick = {}
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                ),
                text = "Close session"
            )
        }
    }
}

@Composable
private fun MetricsUnitsBox(
    onSaveButtonPress: (SystemSettingsConfiguration) -> Unit,
    profileSettings: SystemSettingsConfiguration,
    modifier: Modifier = Modifier
) {
    var isDistanceDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    var isVelocityDropDownExpanded by rememberSaveable { mutableStateOf(false) }
    var isSaveChangeButtonActive by rememberSaveable { mutableStateOf(false) }

    var distanceUnitsMetrics by rememberSaveable { mutableStateOf(profileSettings.distanceUnitsMetrics) }
    var velocityUnitsMetrics by rememberSaveable { mutableStateOf(profileSettings.velocityUnitsMetrics) }

    LaunchedEffect(distanceUnitsMetrics) {
        if(distanceUnitsMetrics != profileSettings.distanceUnitsMetrics) isSaveChangeButtonActive = true
    }
    LaunchedEffect(velocityUnitsMetrics) {
        if(velocityUnitsMetrics != profileSettings.velocityUnitsMetrics) isSaveChangeButtonActive = true
    }

    Surface(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "telemetry icon",
                    modifier = Modifier.size(40.dp),
                    tint = signalGreen
                )
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "Tracking"
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Distance"
                )
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    OutlinedButton(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { isDistanceDropDownExpanded = !isDistanceDropDownExpanded }
                    ) {
                        AnimatedContent(
                            targetState = when(distanceUnitsMetrics) {
                                DistanceUnitsMetrics.KM -> "Kilometer"
                                DistanceUnitsMetrics.MIL-> "Mille"
                            }
                        ) { distanceUnitsMetric ->
                            Text(
                                text = distanceUnitsMetric
                            )
                        }
                    }
                    DropdownMenu(
                        border = BorderStroke(
                            1.dp, Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(0.8f),
                                    MaterialTheme.colorScheme.surfaceContainer
                                )
                            )
                        ),
                        shape = RoundedCornerShape(15.dp),
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = isDistanceDropDownExpanded,
                        onDismissRequest = { isDistanceDropDownExpanded = false },
                        modifier = Modifier.fillMaxWidth().padding(
                            vertical = 10.dp,
                            horizontal = 20.dp
                        )
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Kilometer") },
                            onClick = {
                                distanceUnitsMetrics = DistanceUnitsMetrics.KM
                                isDistanceDropDownExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Mille") },
                            onClick = {
                                distanceUnitsMetrics = DistanceUnitsMetrics.MIL
                                isDistanceDropDownExpanded = false
                            }
                        )
                    }
                }

            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Velocity"
                )
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    OutlinedButton(
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { isVelocityDropDownExpanded = !isVelocityDropDownExpanded }
                    ) {
                        AnimatedContent(
                            targetState = when(velocityUnitsMetrics) {
                                VelocityUnitsMetrics.KMHrs -> "Km/h"
                                VelocityUnitsMetrics.MilHrs -> "Mph"
                            }
                        ) { velocityUnitsMetric ->
                            Text(
                                text = velocityUnitsMetric
                            )
                        }
                    }
                    DropdownMenu(
                        border = BorderStroke(
                            1.dp, Brush.linearGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(0.8f),
                                    MaterialTheme.colorScheme.surfaceContainer
                                )
                            )
                        ),
                        shape = RoundedCornerShape(15.dp),
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = isVelocityDropDownExpanded,
                        onDismissRequest = { isVelocityDropDownExpanded = false },
                        modifier = Modifier.fillMaxWidth().padding(
                            vertical = 10.dp,
                            horizontal = 20.dp
                        )
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Km/h") },
                            onClick = {
                                velocityUnitsMetrics = VelocityUnitsMetrics.KMHrs
                                isVelocityDropDownExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Mph") },
                            onClick = {
                                velocityUnitsMetrics = VelocityUnitsMetrics.MilHrs
                                isVelocityDropDownExpanded = false
                            }
                        )
                    }
                }

            }
            Button(
                enabled = isSaveChangeButtonActive,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSaveButtonPress(
                        profileSettings.copy(
                            distanceUnitsMetrics = distanceUnitsMetrics,
                            velocityUnitsMetrics = velocityUnitsMetrics
                        )
                    )
                }
            ) {
                Text(
                    text = "Update Telemetry configs "
                )
            }
        }
    }
}

@Composable
private fun RefreshRatingBox(
    onSaveButtonPress: (SystemSettingsConfiguration) -> Unit,
    profileConfigs: SystemSettingsConfiguration,
    modifier: Modifier = Modifier
) {
    var rateValue by rememberSaveable { mutableFloatStateOf(profileConfigs.refreshRate) }
    var isAutoStabilizationActive by rememberSaveable {
        mutableStateOf(profileConfigs.isAutoStabilized)
    }
    var isSaveChangeButtonActive by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(rateValue) {
        if(rateValue != profileConfigs.refreshRate) isSaveChangeButtonActive = true
    }
    LaunchedEffect(isAutoStabilizationActive) {
        if(isAutoStabilizationActive != profileConfigs.isAutoStabilized) isSaveChangeButtonActive = true
    }
    Surface(
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = "tracker icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    style = MaterialTheme.typography.headlineSmall,
                    text = "Tracking"
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        text = "Refresh Rate"
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Update frequency of telemetry data"
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Slider(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 7.dp),
                        value = rateValue,
                        onValueChange = { rateValue = it },
                        steps = 6,
                        valueRange = 200f..2000f
                    )
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        text = "$rateValue ms"
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                ) {
                    Text(
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        text = "Auto-Stabilization"
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                        style = MaterialTheme.typography.bodyLarge,
                        text = "Maintain signal lock during orbital drift"
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Switch(
                        checked = isAutoStabilizationActive,
                        onCheckedChange = { isAutoStabilizationActive = !isAutoStabilizationActive }
                    )
                    AnimatedContent(
                        targetState = if(isAutoStabilizationActive) "Active" else ""
                    ) { activeState ->
                        Text(
                            color = signalGreen,
                            fontWeight = FontWeight.Bold,
                            text = activeState
                        )
                    }
                }
            }
            Button(
                enabled = isSaveChangeButtonActive,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSaveButtonPress(
                        profileConfigs.copy(
                            refreshRate = rateValue,
                            isAutoStabilized = isAutoStabilizationActive
                        )
                    )
                }
            ) {
                Text(
                    text = "Update tracking config"
                )
            }
        }
    }
}

@Composable
private fun ProfileBox(
    user: User,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 3.dp,
        tonalElevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Surface(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    shadowElevation = 10.dp,
                    tonalElevation = 8.dp,
                    shape = CircleShape,
                    modifier = Modifier.size(120.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "profile photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surfaceContainer.copy(0.8f),

                                        )
                                )
                            )
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(15.dp),
                        shape = CircleShape,
                        color = when (user.accountState) {
                            AccountState.ACTIVE -> signalGreen
                            AccountState.IN_VERIFICATION -> signalAmber
                            AccountState.BLOCKED -> telemetryRed
                        }
                    ) {}
                }
            }
            Text(
                text = user.userName,
                style = MaterialTheme.typography.headlineLarge
            )
            Surface(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                color = MaterialTheme.colorScheme.primary.copy(0.2f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = "badge"
                    )
                    Text(
                        text = user.rank.name + " RANK"
                    )
                }
            }
            Button(
                shape = RoundedCornerShape(15.dp),
                onClick = {}
            ) {
                Text(text = "EDIT PROFILE")
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val profileConfig = SystemSettingsConfiguration(
        refreshRate = 200f,
        isAutoStabilized = false,
        distanceUnitsMetrics = DistanceUnitsMetrics.KM,
        velocityUnitsMetrics = VelocityUnitsMetrics.KMHrs
    )
    val user = User(
        Random.nextLong(),
        "userTest",
        "test@mail.com",
        "23if2e",
        "",
        UserRank.OPERATOR,
        AccountState.ACTIVE,
        profileConfig)
    SatExplorerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileScreen(
                user = user,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    }
}
