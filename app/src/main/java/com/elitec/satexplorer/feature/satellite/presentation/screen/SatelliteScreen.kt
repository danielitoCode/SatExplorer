package com.elitec.satexplorer.feature.satellite.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Vertices
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.feature.tracking.domain.entity.Satellite
import com.elitec.satexplorer.feature.tracking.domain.entity.SatelliteType
import com.elitec.satexplorer.feature.tracking.domain.entity.TleData
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import com.elitec.satexplorer.infrastructure.presentation.theme.telemetryRed
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun SatelliteScreen(
    satelliteList: List<Satellite>,
    modifier: Modifier = Modifier
) {
    var searchCriterial by rememberSaveable { mutableStateOf("") }
    var satelliteTypeSelected by rememberSaveable { mutableStateOf<SatelliteType?>(null) }
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*Icon(
                painter = painterResource(R.drawable.parabolical),
                contentDescription = "parabolical icon",
                modifier = Modifier.size(30.dp)
            )*/
            Text(
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                text = "MISSION CONTROL"
            )
        }
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(0.7f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            value = searchCriterial,
            onValueChange = { searchCriterial = it },
            label = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon"
                    )
                    Text(
                        text = "Search satellite"
                    )
                }
            },
            placeholder = {
                Text(
                    text = "Search satellite by name or NORAD"
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(SatelliteType.toList()) { satelliteType ->
                Button(
                    border = BorderStroke(
                        1.dp, MaterialTheme.colorScheme.primary.copy(0.5f)
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = animateColorAsState(
                            if(satelliteType == satelliteTypeSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ).value,
                        contentColor = animateColorAsState(
                            if(satelliteType == satelliteTypeSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ).value,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    onClick = { satelliteTypeSelected = satelliteType }
                ) {
                    Text(
                        text = satelliteType.name
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(satelliteList) { satellite ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 3.dp,
                        pressedElevation = 1.dp,
                        focusedElevation = 5.dp,
                        hoveredElevation = 5.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                        style = MaterialTheme.typography.titleSmall,
                                        text = "NORAD:"
                                    )
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        text = "NORAD"
                                    )
                                }
                                Text(
                                    style = MaterialTheme.typography.titleLarge,
                                    text = satellite.name
                                )
                            }

                            Surface(
                                color = if(satellite.isActive) signalGreen.copy(0.1f) else telemetryRed.copy(0.1f),
                                tonalElevation = 2.dp,
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    color = if(satellite.isActive) signalGreen else telemetryRed,
                                    text = if(satellite.isActive) "Active" else "Inactive"
                                )
                            }
                        }
                        Box(
                            contentAlignment = Alignment.BottomStart,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .heightIn(max = 200.dp)
                                .clip(RoundedCornerShape(15.dp))
                        ) {
                            Image(
                                painter = painterResource(R.drawable.banner),
                                contentDescription = "satellite type photo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                            )
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = "Launch date:"
                                )
                                Text(
                                    text = satellite.launchDate.toString()
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {

                                    Text(
                                        style = MaterialTheme.typography.labelLarge,
                                        text = "Altitude"
                                    )
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        text = "${satellite.tle.raan} Km"
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {

                                    Text(
                                        style = MaterialTheme.typography.labelLarge,
                                        text = "Type"
                                    )
                                    Text(
                                        style = MaterialTheme.typography.titleMedium,
                                        text = satellite.type.name
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun SatelliteScreenPreview() {
    SatExplorerTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            var satelliteList = mutableListOf<Satellite>()
            val count = 1..9
            count.forEach { item ->
                satelliteList.add(
                    Satellite(
                        item.toLong(),
                        item,
                        "Satellite $item",
                        SatelliteType.getRandomType(),
                        TleData(
                            "line1 $item",
                            "line2 $item",
                            Clock.System.now().toEpochMilliseconds(),
                            Random.nextInt().toDouble(),
                            Random.nextInt().toDouble(),
                            Random.nextInt().toDouble(),
                            Random.nextInt().toDouble(),
                            Random.nextInt().toDouble(),
                            Random.nextInt().toDouble()
                        ),
                        launchDate = Clock.System.now().toEpochMilliseconds(),
                        isActive = true
                    )
                )
            }
            SatelliteScreen(
                satelliteList = satelliteList,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
