package com.elitec.satexplorer.infrastructure.presentation

import android.widget.Space
import androidx.collection.mutableIntSetOf
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.elitec.satexplorer.R
import com.elitec.satexplorer.infrastructure.presentation.components.LiveAnimatedBox
import com.elitec.satexplorer.infrastructure.presentation.theme.SatExplorerTheme
import com.elitec.satexplorer.infrastructure.presentation.theme.signalGreen
import kotlinx.coroutines.delay
import kotlin.concurrent.timer

@Composable
fun OnBoardScreen(
    modifier: Modifier = Modifier
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(1) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.iconclean),
                contentDescription = "app icon",
                modifier = Modifier.size(70.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = "SatEXPLORER"
                )
                LiveAnimatedBox()
            }
        }
        OnBoardBody(
            index = selectedIndex,
            modifier = Modifier.fillMaxWidth()
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            OnBoardIndicator(
                itemSpacing = 10.dp,
                index = selectedIndex
            )
            AnimatedVisibility(
                visible = selectedIndex != 3
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = {}
                    ) {
                        Text(
                            text = "Skip"
                        )
                    }
                    Button(
                        onClick = { selectedIndex++ }
                    ) {
                        Text(
                            text = "Next"
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = selectedIndex == 3
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                ) {
                    Text(
                        text = "GET STARTED"
                    )
                }
            }
        }
    }
}

@Composable
fun OnBoardIndicator(
    index: Int,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 5.dp,
) {
    val firstOnBoardWidth = if(index == 1) 60.dp else 10.dp
    val secondOnBoardWidth = if(index == 2) 60.dp else 10.dp
    val thirdOnBoardWidth = if(index == 3) 60.dp else 10.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing),
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = firstOnBoardWidth
        ) { width ->
            Surface(
                modifier = Modifier.size(width = width, height = 10.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.onBackground
            ) { }
        }
        AnimatedContent(
            targetState = secondOnBoardWidth
        ) { width ->
            Surface(
                modifier = Modifier.size(width = width, height = 10.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.onBackground
            ) { }
        }
        AnimatedContent(
            targetState = thirdOnBoardWidth
        ) { width ->
            Surface(
                modifier = Modifier.size(width = width, height = 10.dp),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.onBackground
            ) { }
        }
    }
}

@Composable
private fun ARTrackingOnBoardSection(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(200.dp, 350.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.onboard2),
                contentDescription = "onBoard picture 1",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
            )

            OutlinedCard(
                modifier = Modifier.align(Alignment.TopEnd),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        text = "Tracking lock"
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            tint = signalGreen,
                            imageVector = Icons.Default.GpsFixed,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "gps track icon",
                        )
                        Text(
                            fontWeight = FontWeight.Bold,
                            color = signalGreen,
                            style = MaterialTheme.typography.bodyMedium,
                            text = "98.4 % ACCURACY"
                        )
                    }
                }
            }
            OutlinedCard(
                modifier = Modifier.align(Alignment.BottomStart),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    2.dp,
                    MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = "Satellites in view"
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                        text = "+ 15 Actives"
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                text = "Explore the sky with"
            )
            Spacer(Modifier.width(10.dp))
            Text(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                text = "AR"
            )
        }
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            text = "Point your device at the sky to identify satellites in real-time. Experience the orbit like never before with high-precision AR tracking"
        )
    }
}

@Composable
private fun OrbitalOnBoardSection(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(200.dp, 350.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.onboard1),
                contentDescription = "onBoard picture 1",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(0.5f),
                                MaterialTheme.colorScheme.background,
                            )
                        )
                    )
            )
            OutlinedCard(
                modifier = Modifier.align(Alignment.TopStart),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    2.dp,
                    Brush.linearGradient(
                        tileMode = TileMode.Decal,
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        text = "Inclination"
                    )
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "97.45°"
                    )
                }
            }
            OutlinedCard(
                modifier = Modifier.align(Alignment.BottomEnd),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(
                    2.dp,
                    Brush.linearGradient(
                        tileMode = TileMode.Decal,
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                ),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        text = "Altitude"
                    )
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "542 Km"
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                text = "Track Earth In"
            )
            Spacer(Modifier.width(10.dp))
            Text(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                text = "Real Time"
            )
        }
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            text = "Monitor thousands of satellites with professionals orbital mechanics. Precise telemetry data and yours fingertips "
        )
    }
}

@Composable
private fun ReceptionOnBoardingSection(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant ,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 5.dp,
        tonalElevation = 5.dp
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(50.dp).padding(10.dp),
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "orbital icon"
                    )
                }
                Spacer(
                    modifier = Modifier.width(20.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall,
                        text = "MODULE 03 // DYNAMICS"
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        text = "Real Orbital"
                    )
                    Text(
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        text = "Mechanics"
                    )
                }
            }
            Text(
                textAlign = TextAlign.Justify,
                text = "Powered by aerospace-grade SGP4 algorithms. Access professional TLE data, precise altitude, and velocity telemetry for thousands of objects in real-time sync with orbital propagation models"
            )
            Icon(
                painter = painterResource(R.drawable.parabolical),
                contentDescription = "satellite receptor",
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 4.dp,
                            modifier = Modifier.height(100.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                text = "UPDATE FREQUENCY"
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                text = "100 Hz"
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                text = "POLLING"
                            )
                        }
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 4.dp,
                            modifier = Modifier.height(100.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                                text = "SYNC INTEGRITY"
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                text = "99.9 %"
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge,
                                text = "ACURRACY"
                            )
                        }
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun OnBoardBody(
    index: Int = 1,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = index == 1
    ) {
        OrbitalOnBoardSection()
    }
    AnimatedVisibility(
        visible = index == 2
    ) {
        ARTrackingOnBoardSection()
    }
    AnimatedVisibility(
        visible = index == 3
    ) {
        ReceptionOnBoardingSection()
    }
}

@Preview
@Composable
fun OnBoardScreenPreview () {
    SatExplorerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            OnBoardScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
